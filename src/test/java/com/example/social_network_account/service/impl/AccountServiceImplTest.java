package com.example.social_network_account.service.impl;

import com.example.social_network_account.client.FriendServiceClient;
import com.example.social_network_account.dto.AccountByFilterDto;
import com.example.social_network_account.dto.AccountSearchDto;
import com.example.social_network_account.dto.RegistrationEventDto;
import com.example.social_network_account.entity.Account;
import com.example.social_network_account.exception.AccountNotFoundException;
import com.example.social_network_account.repository.AccountRepository;
import com.example.social_network_account.security.SecurityUtils;
import com.example.social_network_account.service.KafkaEventService;
import com.example.social_network_account.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Spy
    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository repository;

    @Mock
    private KafkaEventService kafkaEventService;

    @Mock
    private FriendServiceClient friendServiceClient;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private AccountSearchDto accountSearchDto;

    @Mock
    private Page<Account> accountPage;

    @Mock
    private Pageable pageable;

    private UUID accountId;
    private Account account;
    private Account account1;
    private Account newAccount;
    private AccountByFilterDto filterDto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        account = new Account();
        account.setId(accountId);
        account.setBlocked(false);
        account.setDeleted(false);
        account.setEmail("test@example.com");
        account.setOnline(false);
        account.setLastOnlineTime(null);
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setPhone("0987654399");

        account1 = new Account();
        account1.setId(UUID.randomUUID());
        account1.setFirstName("Jane");
        account1.setLastName("Smith");

        newAccount = new Account();
        newAccount.setFirstName("Jane");
        newAccount.setLastName("Smith");
        newAccount.setEmail("changedTest@example.com");
        newAccount.setPhone("0987654321");

        filterDto = new AccountByFilterDto();
        filterDto.setAccountSearchDto(accountSearchDto);
        filterDto.setPageNumber(0);
        filterDto.setPageSize(10);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getAccount_Success() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.getAccount();

        assertNotNull(result);
        assertEquals(accountId, result.getId());
    }

    @Test
    void getAccount_AccountNotFound() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount());
    }

    @Test
    void getAccount_AccountBlocked() {
        account.setBlocked(true);
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount());
    }

    @Test
    void getAccount_AccountDeleted() {
        account.setDeleted(true);
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount());
    }

    @Test
    void isOnline_Success() {
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.isOnline(accountId.toString());

        assertTrue(account.isOnline());
        assertNotNull(account.getLastOnlineTime());
        verify(repository).save(account);
    }

    @Test
    void isOnline_AccountNotFound() {
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.isOnline(accountId.toString()));
    }

    @Test
    void isNotOnline_Success() {
        accountService.isNotOnline();

        verify(repository).updateOfflineStatus();
    }

    @Test
    void changedEmail_Success() {
        String newEmail = "newTest@example.com";
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.changedEmail(accountId, newEmail);

        assertEquals(newEmail, account.getEmail());
        verify(repository).save(account);
    }

    @Test
    void changedEmail_AccountNotFound() {
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.changedEmail(accountId, "newEmail@example.com"));
    }

    @Test
    void updateAccount_Success() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        when(repository.findById(accountId)).thenReturn(Optional.of(account));
        when(repository.save(any(Account.class))).thenReturn(account);

        Account updatedAccountResult = accountService.updateAccount(newAccount);

        assertNotNull(updatedAccountResult, "updatedAccount is null after updateAccount call");
        assertEquals("Jane", updatedAccountResult.getFirstName());
        assertEquals("Smith", updatedAccountResult.getLastName());
        verify(repository).save(updatedAccountResult);
    }

    @Test
    void updateAccount_OldAccountNotFound() {
        when(repository.findById(null)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(newAccount));
    }

    @Test
    void updateFields_Success() {
        Account updatedAccount = accountService.updateFields(account, newAccount);

        assertEquals("Jane", updatedAccount.getFirstName());
        assertEquals("Smith", updatedAccount.getLastName());
        assertEquals("0987654321", updatedAccount.getPhone());
    }

    @Test
    void updateFields_OldAccountNull() {
        assertThrows(IllegalArgumentException.class, () -> accountService.updateFields(null, newAccount));
    }

    @Test
    void deleteAccount_Success() {
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.deleteAccount(accountId);

        assertTrue(account.isDeleted());
        verify(repository).save(account);
        verify(kafkaEventService).deletedAccountEvent(account.getId());
    }

    @Test
    void deleteAccount_AccountNotFound() {
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(accountId));
    }

    @Test
    void blockAccount_Success() {
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.blockAccount(accountId);

        assertTrue(account.isBlocked());
        verify(repository).save(account);
        verify(kafkaEventService).blockedAccountEvent(account.getId());
    }

    @Test
    void blockAccount_AccountNotFound() {
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.blockAccount(accountId));
    }

    @Test
    void unblockAccount_Success() {
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.unblockAccount(accountId);

        assertFalse(account.isBlocked());
        verify(repository).save(account);
        verify(kafkaEventService).unBlockedAccountEvent(account.getId());
    }

    @Test
    void unblockAccount_AccountNotFound() {
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.unblockAccount(accountId));
    }

    @Test
    void getAllAccounts_Success() {
        accountPage = new PageImpl<>(List.of(account, account1), pageable, 2);

        when(repository.findAll(pageable)).thenReturn(accountPage);

        Page<Account> result = accountService.getAllAccounts(pageable);

        assertNotNull(result, "Result should not be null");

        assertEquals(2, result.getTotalElements(), "Total elements should be 2");

        assertEquals("John", result.getContent().get(0).getFirstName(), "First account's first name should be John");
        assertEquals("Jane", result.getContent().get(1).getFirstName(), "Second account's first name should be Jane");

        verify(repository).findAll(pageable);
    }

    @Test
    void getAllAccounts_ShouldReturnEmptyPage_WhenNoAccountsExist() {

        accountPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(repository.findAll(pageable)).thenReturn(accountPage);

        Page<Account> result = accountService.getAllAccounts(pageable);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.getContent().isEmpty(), "Content should be empty");
        assertEquals(0, result.getTotalElements(), "Total elements should be 0");

        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void getAllAccounts_ShouldReturnPagedResults_WhenAccountsExist() {
        List<Account> accounts = List.of(account, account1);
        accountPage = new PageImpl<>(accounts, pageable, 2);

        when(repository.findAll(pageable)).thenReturn(accountPage);

        Page<Account> result = accountService.getAllAccounts(pageable);

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.getTotalElements(), "Total elements should be 2");
        assertEquals("John", result.getContent().get(0).getFirstName(), "First account's first name should be John");
        assertEquals("Jane", result.getContent().get(1).getFirstName(), "Second account's first name should be Jane");

        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void createAccount_success() {
        RegistrationEventDto registrationEvent = new RegistrationEventDto();
        registrationEvent.setEmail("test@example.com");
        registrationEvent.setFirstName("Test");
        registrationEvent.setLastName("User");
        registrationEvent.setUserId(UUID.randomUUID());

        when(repository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(registrationEvent);

        assertNotNull(createdAccount);
        assertEquals("test@example.com", createdAccount.getEmail());
        assertEquals("Test", createdAccount.getFirstName());
        assertEquals("User", createdAccount.getLastName());
        assertNotNull(createdAccount.getRegDate());
        assertNotNull(createdAccount.getCreatedOn());
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    void searchAccounts_WithValidDtoAndShowFriendsTrue() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        AccountSearchDto searchDto = new AccountSearchDto();
        boolean showFriends = true;

        List<UUID> friendsIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        Page<Account> expectedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(accountService.getFriendsIds()).thenReturn(friendsIds);
        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Account> result = accountService.searchAccounts(searchDto, pageable, showFriends);

        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getTotalElements(), "Total elements should be 0");

        assertEquals(friendsIds, searchDto.getIds(), "Friends IDs should be set in searchDto");
    }

    @Test
    void searchAccounts_WithValidDtoAndShowFriendsFalse() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        AccountSearchDto searchDto = new AccountSearchDto();
        boolean showFriends = false;

        accountPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(accountPage);

        Page<Account> result = accountService.searchAccounts(searchDto, pageable, showFriends);

        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getTotalElements(), "Total elements should be 0");
    }

    @Test
    void searchAccounts_AccountSearchDtoIsNull() {

        assertThrows(AccountNotFoundException.class, () ->
            accountService.searchAccounts(null, pageable, true)
        );
    }

    @Test
    void searchByFilter_ShouldReturnAccounts_WhenFilterMatches() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        List<Account> accounts = List.of(account, account1);
        accountPage = new PageImpl<>(accounts, pageable, accounts.size());

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(accountPage);
        when(accountService.getAccount()).thenReturn(account);

        Page<Account> result = accountService.searchByFilter(filterDto);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements(), "Total elements should be 2");
        assertEquals("John", result.getContent().get(0).getFirstName(), "First account should be John");
        assertEquals("Jane", result.getContent().get(1).getFirstName(), "Second account should be Jane");
        verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchByFilter_ShouldReturnEmptyPage_WhenNoFilterProvided() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

       accountPage = Page.empty(pageable);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(accountPage);
        when(accountService.getAccount()).thenReturn(account);

        Page<Account> result = accountService.searchByFilter(filterDto);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Page should be empty when no filter is applied");
        verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchByFilter_ShouldReturnEmptyPage_WhenFilterDoesNotMatch() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        Page<Account> emptyPage = Page.empty(pageable);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);
        when(accountService.getAccount()).thenReturn(account);

        Page<Account> result = accountService.searchByFilter(filterDto);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Page should be empty when no matching data found");
        verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchByFilter_ShouldReturnPagedResults_WhenPaginationIsApplied() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        List<Account> accounts = List.of(account, account1);
       accountPage = new PageImpl<>(accounts, pageable, accounts.size());

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(accountPage);
        when(accountService.getAccount()).thenReturn(account);

        Page<Account> result = accountService.searchByFilter(filterDto);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements(), "Total elements should be 2");
        assertEquals(1, result.getTotalPages(), "Should return one page");

        assertTrue(result.isLast(), "Should be the last page");
        assertFalse(result.hasNext(), "Should not have a next page");

        verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchByFilter_ShouldThrowException_WhenGetAccountFails() {
        when(securityUtils.getAccountId()).thenReturn(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        when(accountService.getAccount()).thenThrow(new AccountNotFoundException("Account not found", HttpStatus.NOT_FOUND));

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.searchByFilter(filterDto);
        });
    }

    @Test
    void getAccountById_Success() {
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountById(accountId);

        assertNotNull(foundAccount, "Account should not be null");
        assertEquals(accountId, foundAccount.getId(), "Account ID should match");
        verify(repository).findById(accountId);
    }

    @Test
    void getAccountById_AccountNotFound() {
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(accountId));
    }

    @Test
    void getFriendsIds_Success() {
        List<UUID> friendsIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(friendServiceClient.getFriendsIds()).thenReturn(friendsIds);

        List<UUID> result = accountService.getFriendsIds();

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "There should be 2 friends IDs");
        verify(friendServiceClient).getFriendsIds();
    }

    @Test
    void getFriendsIds_ShouldReturnEmptyList_WhenNoFriends() {
        when(friendServiceClient.getFriendsIds()).thenReturn(Collections.emptyList());

        List<UUID> result = accountService.getFriendsIds();

        assertNotNull(result, "Результат не должен быть null");
        assertTrue(result.isEmpty(), "Список должен быть пустым");
        verify(friendServiceClient).getFriendsIds();
    }

    @Test
    void getFriendsIds_ShouldThrowException_WhenClientThrowsException() {
        when(friendServiceClient.getFriendsIds()).thenThrow(new RuntimeException("Ошибка при получении друзей"));

        Exception exception = assertThrows(RuntimeException.class, () -> accountService.getFriendsIds());
        assertEquals("Ошибка при получении друзей", exception.getMessage());
        verify(friendServiceClient).getFriendsIds();
    }

    @Test
    void getFriendsIds_ShouldHandleNullResponse_FromClient() {
        when(friendServiceClient.getFriendsIds()).thenReturn(null);

        List<UUID> result = accountService.getFriendsIds();

        assertNotNull(result, "Результат не должен быть null");
        assertTrue(result.isEmpty(), "Список должен быть пустым при null-ответе от клиента");
        verify(friendServiceClient).getFriendsIds();
    }

    @Test
    void searchAccounts_nullDto_throwsException() {
        assertThrows(AccountNotFoundException.class, () -> accountService.searchAccounts(null, PageRequest.of(0, 10), false));
    }
}