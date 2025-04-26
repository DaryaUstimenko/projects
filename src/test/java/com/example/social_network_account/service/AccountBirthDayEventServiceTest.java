package com.example.social_network_account.service;

import com.example.social_network_account.entity.Account;
import com.example.social_network_account.repository.AccountRepository;
import com.example.social_network_account.service.AccountBirthDayEventService;
import com.example.social_network_account.service.KafkaEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountBirthDayEventServiceTest {

    @Mock
    private AccountRepository repository;

    @Mock
    private KafkaEventService kafkaEventService;

    @Spy
    @InjectMocks
    private AccountBirthDayEventService accountBirthDayEventService;

    @Test
    void sendBirthdayNotifications_ShouldSendNotifications_WhenAccountsWithBirthdaysExist() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setFirstName("Иван");
        account.setLastName("Иванов");
        account.setBirthDate(LocalDate.of(1990, 4, 8));

        List<UUID> accountsWithBirthdays = Collections.singletonList(accountId);

        when(repository.findAccountsWithBirthdaysToday()).thenReturn(accountsWithBirthdays);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        accountBirthDayEventService.sendBirthdayNotifications();

        verify(kafkaEventService, times(1)).accountBirthDayEvent(
                accountId,
                account.getBirthDate().toString(),
                account.getLastName() + " " + account.getFirstName()
        );
    }

    @Test
    void sendBirthdayNotifications_ShouldNotSendNotifications_WhenNoAccountsWithBirthdays() {
        when(repository.findAccountsWithBirthdaysToday()).thenReturn(Collections.emptyList());

        accountBirthDayEventService.sendBirthdayNotifications();

        verify(kafkaEventService, never()).accountBirthDayEvent(any(), any(), any());
    }
}