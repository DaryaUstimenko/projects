package com.example.social_network_account.controller;

import com.example.social_network_account.dto.*;
import com.example.social_network_account.entity.Account;
import com.example.social_network_account.exception.AccountNotFoundException;
import com.example.social_network_account.mapper.AccountMapper;
import com.example.social_network_account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getAccount() {
        Account account = accountService.getAccount();
        return accountMapper.accountEntityToAccountDto(account);
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto updateAccount(@RequestBody AccountDto accountDto) {
        Account newAccount = accountService.updateAccount(accountMapper.accountDtoToAccountEntity(accountDto));
        return accountMapper.accountEntityToAccountDto(newAccount);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public String deleteAccount() {
        Account account = accountService.getAccount();
        accountService.deleteAccount(account.getId());
        return "Account deleted";
    }

    @PutMapping("/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String blockAccount(@PathVariable UUID id) {
        accountService.blockAccount(id);
        return "Your account is blocked";
    }

    @DeleteMapping("/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String unblockAccount(@PathVariable UUID id) {
        accountService.unblockAccount(id);
        return "Your account has been unblocked";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageAccountDto getAllAccounts(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id,asc") List<String> sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accounts = accountService.getAllAccounts(pageable);
        List<AccountDto> content = accounts.stream().map(accountMapper::accountEntityToAccountDto).toList();

        return accountService.getPageAccountDto(accounts, pageable, sort, content);
    }

    @PostMapping("/searchByFilter")
    @ResponseStatus(HttpStatus.OK)
    public PageAccountDto searchByFilter(@RequestBody AccountByFilterDto filter) {
        Page<Account> accounts = accountService.searchByFilter(filter);
        List<AccountDto> content = accounts.stream().map(accountMapper::accountEntityToAccountDto).toList();
        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize());
        return accountService.getPageAccountDto(
                accounts, pageable, null, content);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getAccountById(@PathVariable UUID id) {
        Account account = accountService.getAccountById(id);
        return accountMapper.accountEntityToAccountDto(account);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public PageAccountDto searchAccount(@ModelAttribute AccountSearchDto accountSearchDto,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id,asc") List<String> sort,
                                        @RequestParam(required = false) boolean showFriends) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accounts = accountService.searchAccounts(accountSearchDto, pageable, showFriends);
        List<AccountDto> content = accounts.stream().map(accountMapper::accountEntityToAccountDto).toList();
        return accountService.getPageAccountDto(accounts, pageable, sort, content);
    }

    @GetMapping("/accountIds")
    @ResponseStatus(HttpStatus.OK)
    public PageAccountDto getAllAccountsByIds(@RequestParam(required = false) List<UUID> ids,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id,asc") List<String> sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accounts = accountService.getAccountsByIds(pageable, ids);
        List<AccountDto> content = accounts.stream().map(accountMapper::accountEntityToAccountDto).toList();
        return accountService.getPageAccountDto(accounts, pageable, sort, content);
    }
}
