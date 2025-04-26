package com.example.social_network_account.service;

import com.example.social_network_account.entity.Account;
import com.example.social_network_account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountBirthDayEventService {

    private final AccountRepository repository;
    private final KafkaEventService kafkaEventService;

    @Scheduled(cron = "0 0 1 * * *")
    // Запускается ежедневно в 1:00(либо в 3:00), так как время на сервере на 3 часа отличается от сервиса
    public void sendBirthdayNotifications() {
        List<UUID> accountsWithBirthdays = repository.findAccountsWithBirthdaysToday();
        if (!accountsWithBirthdays.isEmpty()) {
            for (UUID accountId : accountsWithBirthdays) {
                log.info("AccountsWithBirthdays: " + accountId);
                Account account = repository.findById(accountId).orElseThrow();
                try {
                    kafkaEventService.accountBirthDayEvent(account.getId(), String.valueOf(account.getBirthDate()),
                            account.getLastName() + " " + account.getFirstName());
                    log.info("Сообщение о дне рождения отправлено в Kafka для {}",
                            account.getLastName() + " " + account.getFirstName());
                } catch (KafkaException e) {
                    log.error("Ошибка отправки сообщения о дне рождения в Kafka: {}", e.getMessage(), e);
                }
            }
        }
    }
}
