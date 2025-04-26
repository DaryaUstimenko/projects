package com.example.social_network_account.service;

import com.example.social_network_account.dto.*;
import com.example.social_network_account.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    Account getAccount();

    void isOnline(String accountId);

    void changedEmail(UUID accountId, String email);

    Account updateAccount(Account newAccount);

    void deleteAccount(UUID id);

    void blockAccount(UUID id);

    void unblockAccount(UUID id);

    Page<Account> getAllAccounts(Pageable pageable);

    PageAccountDto getPageAccountDto(Page<Account> accountPage, Pageable pageable,
                                     List<String> sortList, List<AccountDto> content);

    Account createAccount(RegistrationEventDto registrationEventDto);

    Page<Account> searchByFilter(AccountByFilterDto filterDto);

    Page<Account> searchAccounts(AccountSearchDto accountSearchDto, Pageable pageable, boolean showFriends);

    Account getAccountById(UUID id);

    Page<Account> getAccountsByIds(Pageable pageable, List<UUID> ids);

    List<UUID> getFriendsIds();
}
