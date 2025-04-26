package com.example.social_network_account;

import com.example.social_network_account.client.AuthServiceClient;
import com.example.social_network_account.controller.AccountController;
import com.example.social_network_account.dto.AccountByFilterDto;
import com.example.social_network_account.dto.AccountDto;
import com.example.social_network_account.dto.AccountSearchDto;
import com.example.social_network_account.dto.PageAccountDto;
import com.example.social_network_account.entity.Account;
import com.example.social_network_account.mapper.AccountMapper;
import com.example.social_network_account.repository.AccountRepository;
import com.example.social_network_account.security.JwtTokenFilter;
import com.example.social_network_account.security.SecurityUtils;
import com.example.social_network_account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
class SocialNetworkAccountApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository accountRepository;
    @MockBean
    private AccountService accountService;
    @Autowired
    private AccountController accountController;
    @MockBean
    private AuthServiceClient authServiceClient;
    @MockBean
    private SecurityUtils securityUtils;
    @MockBean
    private JwtTokenFilter jwtTokenFilter;
    @MockBean
    private AccountMapper accountMapper;

    private String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZWNvQGVtYWlsLmNvbSIsImFjY291bnRJZCI6ImUyMTFhMThiLWMzNTItNDU4Ny04ODg4LWZmNGE3MjliNzQ5YiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTc0NDU1MDI3MCwiZXhwIjoxNzQ0NTUxMTcwfQ.sD7dxvjilQE61tlkcG4esXbGHZ_G3R0ZjbPGPWdpqKk";
    private UUID accountId = UUID.randomUUID();
    private Account account;
    private AccountDto accountDto;
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @BeforeEach
    void setUp() throws ServletException, IOException {
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
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());

        doNothing().when(jwtTokenFilter).doFilterInternal(any(), any(), any());
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        when(securityUtils.getToken()).thenReturn(token);
    }

    @Test
    void testGetAccount() throws Exception {

        when(accountService.getAccount()).thenReturn(account);
        when(accountController.getAccount()).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/me")
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isBlocked").value(false))
                .andExpect(jsonPath("$.isBlocked").value(false))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @Transactional
    void testUpdateAccount() throws Exception {

        when(accountService.updateAccount(any(Account.class))).thenReturn(account);
        when(accountController.updateAccount(accountDto)).thenReturn(accountDto);
        accountRepository.save(account);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account/me")
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @Transactional
    void testDeleteAccount() throws Exception {

        when(accountService.getAccount()).thenReturn(account);
        when(accountController.getAccount()).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/account/me")
                        .header("Authorization", token)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Account deleted"));

        verify(accountService, times(1)).deleteAccount(accountId);
    }

    @Test
    @Transactional
    void testBlockAccount() throws Exception {
        when(accountService.getAccount()).thenReturn(account);
        when(accountController.getAccount()).thenReturn(accountDto);
        doNothing().when(accountService).blockAccount(accountId);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account/block/{id}", accountId)
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Your account is blocked"));

        verify(accountService, times(1)).blockAccount(accountId);
        account.setBlocked(true);

        assertTrue(account.isBlocked(), "Your account is blocked");
    }

    @Test
    @Transactional
    void testUnblockAccount() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/account/block/{id}", accountId)
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Your account has been unblocked"));

        verify(accountService, times(1)).unblockAccount(accountId);
    }

    @Test
    @Transactional
    void testGetAllAccounts() throws Exception {

        Page<Account> page = new PageImpl<>(List.of(account));
        List<AccountDto> content = List.of(accountDto);

        when(accountService.getAllAccounts(any(Pageable.class))).thenReturn(page);
        when(accountService.getPageAccountDto(eq(page), any(Pageable.class), anyList(), eq(content)))
                .thenReturn(new PageAccountDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account")
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testSearchByFilter() throws Exception {
        AccountByFilterDto filterDto = new AccountByFilterDto();
        filterDto.setPageNumber(0);
        filterDto.setPageSize(10);

        Page<Account> page = mock(Page.class);
        when(accountService.searchByFilter(any(AccountByFilterDto.class))).thenReturn(page);
        when(accountMapper.accountEntityToAccountDto(any(Account.class))).thenReturn(new AccountDto());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/searchByFilter")
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
                        .content(new ObjectMapper().writeValueAsString(filterDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAccountById() throws Exception {

        when(accountService.getAccountById(accountId)).thenReturn(account);
        when(accountMapper.accountEntityToAccountDto(account)).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/{id}", accountId)
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testSearchAccount() throws Exception {
        AccountSearchDto accountSearchDto = new AccountSearchDto();
        accountSearchDto.setFirstName("Test");

        Page<Account> page = mock(Page.class);
        when(accountService.searchAccounts(any(AccountSearchDto.class), any(Pageable.class), eq(false)))
                .thenReturn(page);
        when(accountMapper.accountEntityToAccountDto(any(Account.class))).thenReturn(new AccountDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/search")
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
                        .param("firstName", "Test")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllAccountsByIds() throws Exception {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        Page<Account> page = mock(Page.class);
        when(accountService.getAccountsByIds(any(Pageable.class), eq(ids)))
                .thenReturn(page);
        when(accountMapper.accountEntityToAccountDto(any(Account.class))).thenReturn(new AccountDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/accountIds")
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
                        .param("ids", ids.get(0).toString() + "," + ids.get(1).toString())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk());
    }
}