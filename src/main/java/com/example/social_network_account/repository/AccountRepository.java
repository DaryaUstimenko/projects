package com.example.social_network_account.repository;

import com.example.social_network_account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    @Query(value = "SELECT account.id FROM schema_account.account account " +
            "WHERE EXTRACT(MONTH FROM account.birth_date) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(DAY FROM account.birth_date) = EXTRACT(DAY FROM CURRENT_DATE)", nativeQuery = true)
    List<UUID> findAccountsWithBirthdaysToday();

    @Modifying
    @Query(value = "UPDATE schema_account.account account SET is_online = false " +
            "WHERE account.last_online_time < NOW() - INTERVAL '5 minutes' AND account.is_online = true",
            nativeQuery = true)
    void updateOfflineStatus();
}
