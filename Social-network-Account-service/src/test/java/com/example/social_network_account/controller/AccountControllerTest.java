package com.example.social_network_account.controller;

import com.example.social_network_account.dto.PageAccountDto;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.social_network_account.dto.AccountByFilterDto;
import com.example.social_network_account.dto.AccountDto;
import com.example.social_network_account.dto.AccountSearchDto;
import com.example.social_network_account.entity.Account;
import com.example.social_network_account.mapper.AccountMapper;
import com.example.social_network_account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountController accountController;

    private Account account;
    private AccountDto accountDto;
    private UUID accountId;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        accountId = UUID.randomUUID();
        account = new Account();
        account.setId(accountId);
        account.setEmail("john.doe@email.com");
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setBlocked(false);
        account.setDeleted(false);

        accountDto = new AccountDto();
        accountDto.setId(accountId);
        accountDto.setBlocked(false);
        accountDto.setDeleted(false);
        accountDto.setEmail("test@example.com");
        accountDto.setFirstName("John");
        accountDto.setLastName("Doe");
    }

    @Test
    @WithMockUser(username = "testuser")
    void getAccount_ShouldReturnAccountDto() throws Exception {

        when(accountService.getAccount()).thenReturn(account);
        when(accountMapper.accountEntityToAccountDto(account)).thenReturn(accountDto);

        AccountDto result = accountController.getAccount();

        assertEquals(accountDto, result);
        verify(accountService, times(1)).getAccount();
        verify(accountMapper, times(1)).accountEntityToAccountDto(account);
    }

    @Test
    void testUpdateAccount() {

        Account updatedAccountEntity = new Account();
        account.setId(accountId);
        account.setEmail("john.doe@email.com");
        account.setFirstName("John");
        account.setLastName("Doe");
        when(accountMapper.accountDtoToAccountEntity(accountDto)).thenReturn(account);
        when(accountService.updateAccount(account)).thenReturn(updatedAccountEntity);
        AccountDto updatedAccountDto = new AccountDto();
        when(accountMapper.accountEntityToAccountDto(updatedAccountEntity)).thenReturn(updatedAccountDto);

        AccountDto result = accountController.updateAccount(accountDto);

        assertEquals(updatedAccountDto, result);
        verify(accountMapper, times(1)).accountDtoToAccountEntity(accountDto);
        verify(accountService, times(1)).updateAccount(account);
        verify(accountMapper, times(1)).accountEntityToAccountDto(updatedAccountEntity);
    }

    @Test
    void testDeleteAccount_accountExists() {

        when(accountService.getAccount()).thenReturn(account);
        doNothing().when(accountService).deleteAccount(any(UUID.class));

        String result = accountController.deleteAccount();
        assertEquals("Account deleted", result);
        verify(accountService, times(1)).getAccount();
        verify(accountService, times(1)).deleteAccount(account.getId());
    }

    @Test
    void testDeleteAccount_accountNotFound() {
        when(accountService.getAccount()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> accountController.deleteAccount());
        verify(accountService, times(1)).getAccount();
        verify(accountService, never()).deleteAccount(any(UUID.class));
    }

    @Test
    void testBlockAccount() {
        UUID id = UUID.randomUUID();
        doNothing().when(accountService).blockAccount(id);

        String result = accountController.blockAccount(id);
        assertEquals("Your account is blocked", result);
        verify(accountService, times(1)).blockAccount(id);
    }

    @Test
    void testUnblockAccount() {
        UUID id = UUID.randomUUID();
        doNothing().when(accountService).unblockAccount(id);

        String result = accountController.unblockAccount(id);
        assertEquals("Your account has been unblocked", result);
        verify(accountService, times(1)).unblockAccount(id);
    }

    @Test
    void testGetAllAccounts() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        Page<Account> accountPage = new PageImpl<>(accounts, pageable, accounts.size());
        when(accountService.getAllAccounts(pageable)).thenReturn(accountPage);
        when(accountMapper.accountEntityToAccountDto(any(Account.class))).thenReturn(new AccountDto());
        when(accountService.getPageAccountDto(any(), any(), any(), any())).thenReturn(new PageAccountDto());


        PageAccountDto result = accountController.getAllAccounts(0, 10, List.of("id,asc"));
        assertNotNull(result);
        verify(accountService, times(1)).getAllAccounts(pageable);
        verify(accountMapper, times(accounts.size())).accountEntityToAccountDto(any(Account.class));
    }

    @Test
    void testSearchByFilter() {
        AccountByFilterDto filter = new AccountByFilterDto();
        filter.setPageNumber(0);
        filter.setPageSize(10);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        Page<Account> accountPage = new PageImpl<>(accounts, PageRequest.of(0, 10), accounts.size());
        when(accountService.searchByFilter(filter)).thenReturn(accountPage);
        when(accountMapper.accountEntityToAccountDto(any(Account.class))).thenReturn(new AccountDto());
        when(accountService.getPageAccountDto(any(), any(), any(), any())).thenReturn(new PageAccountDto());

        PageAccountDto result = accountController.searchByFilter(filter);
        assertNotNull(result);
        verify(accountService, times(1)).searchByFilter(filter);
        verify(accountMapper, times(accounts.size())).accountEntityToAccountDto(any(Account.class));
    }


    @Test
    void testGetAccountById() {

        when(accountService.getAccountById(accountId)).thenReturn(account);
        when(accountMapper.accountEntityToAccountDto(account)).thenReturn(new AccountDto());

        AccountDto result = accountController.getAccountById(accountId);
        assertNotNull(result);
        verify(accountService, times(1)).getAccountById(accountId);
        verify(accountMapper, times(1)).accountEntityToAccountDto(account);
    }

    @Test
    void testSearchAccount() {
        AccountSearchDto accountSearchDto = new AccountSearchDto();
        Pageable pageable = PageRequest.of(0, 10);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        Page<Account> accountPage = new PageImpl<>(accounts, pageable, accounts.size());
        when(accountService.searchAccounts(accountSearchDto, pageable, false)).thenReturn(accountPage);
        when(accountMapper.accountEntityToAccountDto(any(Account.class))).thenReturn(new AccountDto());
        when(accountService.getPageAccountDto(any(), any(), any(), any())).thenReturn(new PageAccountDto());

        PageAccountDto result = accountController.searchAccount(accountSearchDto, 0, 10, List.of("id,asc"), false);
        assertNotNull(result);
        verify(accountService, times(1)).searchAccounts(accountSearchDto, pageable, false);
        verify(accountMapper, times(accounts.size())).accountEntityToAccountDto(any(Account.class));
    }

    @Test
    void testGetAllAccountsByIds() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        Page<Account> accountPage = new PageImpl<>(accounts, pageable, accounts.size());
        when(accountService.getAccountsByIds(pageable, null)).thenReturn(accountPage);
        List<AccountDto> dtos = new ArrayList<>();
        when(accountMapper.accountEntityToAccountDto(any(Account.class))).thenReturn(new AccountDto());
        when(accountService.getPageAccountDto(any(), any(), any(), any())).thenReturn(new PageAccountDto());

        PageAccountDto result = accountController.getAllAccountsByIds(null, 0, 10, List.of("id,asc"));
        assertNotNull(result);
        verify(accountService, times(1)).getAccountsByIds(pageable, null);
        verify(accountMapper, times(accounts.size())).accountEntityToAccountDto(any(Account.class));
    }
}
