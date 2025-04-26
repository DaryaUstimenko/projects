package com.example.social_network_account.service.impl;

import com.example.social_network_account.client.FriendServiceClient;
import com.example.social_network_account.entity.Account;
import com.example.social_network_account.dto.*;
import com.example.social_network_account.exception.AccountNotFoundException;
import com.example.social_network_account.repository.AccountRepository;
import com.example.social_network_account.repository.specification.AccountSpecification;
import com.example.social_network_account.security.SecurityUtils;
import com.example.social_network_account.service.AccountService;
import com.example.social_network_account.service.KafkaEventService;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    private final KafkaEventService kafkaEventService;

    private final FriendServiceClient friendServiceClient;

    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public Account getAccount() {

        UUID accountId = securityUtils.getAccountId();

        Account account = getAccountById(accountId);
        log.info("getAccount: Account ID: " + accountId);
        if (account.isBlocked() || account.isDeleted()) {
            throw new AccountNotFoundException("Account not found", HttpStatus.NOT_FOUND);
        }
        return account;
    }

    @Transactional
    @Override
    public void isOnline(String accountId) {
        UUID id = UUID.fromString(accountId);
        Account account = getAccountById(id);
        if (account == null) {
            throw new AccountNotFoundException("Account not found", HttpStatus.NOT_FOUND);
        }
        account.setOnline(true);
        account.setLastOnlineTime(ZonedDateTime.now());
        repository.save(account);
    }

    @Transactional
    @Scheduled(cron = "0 0/5 * * * *")
    public void isNotOnline() {
        repository.updateOfflineStatus();
    }

    @Override
    @Transactional
    public void changedEmail(UUID accountId, String email) {
        Account account = getAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account not found", HttpStatus.NOT_FOUND);
        }
        account.setEmail(email);
        repository.save(account);
    }

    @Override
    @Transactional
    public Account updateAccount(Account newAccount) {

        Account oldAccount = getAccount();
        if (oldAccount == null) {
            throw new IllegalArgumentException("oldAccount не может быть null");
        }
        Account updatedAccount = updateFields(getAccountById(oldAccount.getId()), newAccount);
        updatedAccount.setUpdatedOn(ZonedDateTime.now());
        log.info("updateAccount: Account ID: " + oldAccount.getId());

        return repository.save(updatedAccount);
    }


    public Account updateFields(Account oldAccount, Account newAccount) {
        if (oldAccount == null) {
            throw new IllegalArgumentException("oldAccount не может быть null");
        }
        if (!Objects.equals(oldAccount.getFirstName(), newAccount.getFirstName())) {
            oldAccount.setFirstName(newAccount.getFirstName());
        }
        if (!Objects.equals(oldAccount.getLastName(), newAccount.getLastName())) {
            oldAccount.setLastName(newAccount.getLastName());
        }
        if (!Objects.equals(oldAccount.getPhone(), newAccount.getPhone())) {
            oldAccount.setPhone(newAccount.getPhone());
        }
        if (!Objects.equals(oldAccount.getPhoto(), newAccount.getPhoto())) {
            oldAccount.setPhoto(newAccount.getPhoto());
        }
        if (!Objects.equals(oldAccount.getProfileCover(), newAccount.getProfileCover())) {
            oldAccount.setProfileCover(newAccount.getProfileCover());
        }
        if (!Objects.equals(oldAccount.getAbout(), newAccount.getAbout())) {
            oldAccount.setAbout(newAccount.getAbout());
        }
        if (!Objects.equals(oldAccount.getCity(), newAccount.getCity())) {
            oldAccount.setCity(newAccount.getCity());
        }
        if (!Objects.equals(oldAccount.getCountry(), newAccount.getCountry())) {
            oldAccount.setCountry(newAccount.getCountry());
        }
        if (!Objects.equals(oldAccount.getBirthDate(), newAccount.getBirthDate())) {
            oldAccount.setBirthDate(newAccount.getBirthDate());
        }
        if (!Objects.equals(oldAccount.getPhotoId(), newAccount.getPhotoId())) {
            oldAccount.setPhotoId(newAccount.getPhotoId());
        }
        if (!Objects.equals(oldAccount.getPhotoName(), newAccount.getPhotoName())) {
            oldAccount.setPhotoName(newAccount.getPhotoName());
        }
        if (!Objects.equals(oldAccount.getUpdatedOn(), newAccount.getUpdatedOn())) {
            oldAccount.setUpdatedOn(newAccount.getUpdatedOn());
        }
        log.info("Account updated: " + oldAccount);
        return oldAccount;
    }

    @Override
    @Transactional
    public void deleteAccount(UUID id) {
        Account account = getAccountById(id);
        account.setDeleted(true);
        repository.save(account);

        kafkaEventService.deletedAccountEvent(account.getId());
        log.info("Удален аккаунт с ID: {}", account.getId());
    }

    @Override
    @Transactional
    public void blockAccount(UUID id) {
        Account account = getAccountById(id);
        account.setBlocked(true);
        repository.save(account);

        kafkaEventService.blockedAccountEvent(account.getId());
        log.info("Заблокирован аккаунт с ID: {}", id);
    }

    @Override
    @Transactional
    public void unblockAccount(UUID id) {
        Account account = getAccountById(id);
        account.setBlocked(false);
        repository.save(account);

        kafkaEventService.unBlockedAccountEvent(account.getId());
        log.info("Разблокирован аккаунт с ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> getAllAccounts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public PageAccountDto getPageAccountDto(Page<Account> accountPage, Pageable pageable,
                                            List<String> sortList, List<AccountDto> content) {

        Sort sort = createSort(sortList);
        SortObject sortObject = new SortObject(sort.isSorted(), sort.isEmpty(), sort.isUnsorted());

        PageableObject pageableObject = new PageableObject(pageable.isPaged(), pageable.getPageNumber(),
                pageable.getPageSize(), (int) pageable.getOffset(), sortObject, pageable.isUnpaged());

        return new PageAccountDto(
                accountPage.getTotalPages(), accountPage.getTotalElements(),
                pageableObject, accountPage.getSize(), content, accountPage.getNumber(),
                sortObject, accountPage.isFirst(), accountPage.isLast(),
                accountPage.getNumberOfElements(), accountPage.isEmpty());
    }

    private Sort createSort(List<String> sortParams) {
        Sort sort = Sort.unsorted();
        if (sortParams != null && !sortParams.isEmpty()) {
            Sort.Order[] orders = sortParams.stream()
                    .map(this::parseSortParam)
                    .filter(Objects::nonNull)
                    .toArray(Sort.Order[]::new);
            sort = Sort.by(orders);
        }
        return sort;
    }

    @JsonDeserialize
    private Sort.Order parseSortParam(String param) {
        String[] parts = param.split(",");
        if (parts.length != 2) {
            return null;
        }
        String property = parts[0];
        Sort.Direction direction = Sort.Direction.fromString(parts[1].toUpperCase());
        return new Sort.Order(direction, property);
    }

    @Override
    @Transactional
    public Account createAccount(RegistrationEventDto registrationEvent) {
        Account account = new Account();
        account.setEmail(registrationEvent.getEmail());
        account.setFirstName(registrationEvent.getFirstName());
        account.setLastName(registrationEvent.getLastName());
        account.setRegDate(ZonedDateTime.now());
        account.setCreatedOn(ZonedDateTime.now());
        repository.save(account);
        kafkaEventService.createdAccountEvent(account.getId(), registrationEvent.getUserId());
        log.info("Kafka send createdAccountEvent: accountId: {} userId: {}",
                account.getId(), registrationEvent.getUserId());
        log.info("Сохранен аккаунт: {}", account);

        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> searchAccounts(AccountSearchDto accountSearchDto, Pageable pageable, boolean showFriends) {
        if (accountSearchDto == null) {
            throw new AccountNotFoundException("Account search parameters are null", HttpStatus.NOT_FOUND);
        }
        if (showFriends) {
            List<UUID> friendsIds = getFriendsIds();
            accountSearchDto.setIds(friendsIds);
        }
        log.warn("Method searchAccounts");
        UUID currentAccountId = getAccount().getId();
        AccountByFilterDto accountByFilterDto = new AccountByFilterDto(accountSearchDto, pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(AccountSpecification.withFilter(accountByFilterDto, currentAccountId),
                PageRequest.of(accountByFilterDto.getPageNumber(), accountByFilterDto.getPageSize()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> searchByFilter(AccountByFilterDto filterDto) {
        UUID currentAccountId = getAccount().getId();
        log.warn("Method search by filter");
        return repository.findAll(AccountSpecification.withFilter(filterDto, currentAccountId),
                PageRequest.of(filterDto.getPageNumber(), filterDto.getPageSize()));
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> getAccountsByIds(Pageable pageable, List<UUID> ids) {
        if (ids.isEmpty()) {
            return Page.empty();
        }
        log.warn(" Method getAccountsByIds");
        return repository.findAll(AccountSpecification.byAccountIds(ids), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> getFriendsIds() {
        List<UUID> friendsIds = friendServiceClient.getFriendsIds();
        return friendsIds != null ? friendsIds : new ArrayList<>();
    }
}
