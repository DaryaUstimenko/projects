package com.example.social_network_account.repository.specification;

import com.example.social_network_account.dto.AccountByFilterDto;
import com.example.social_network_account.dto.AccountSearchDto;
import com.example.social_network_account.entity.Account;

import com.example.social_network_account.repository.specification.AccountSpecification;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountSpecificationTest {

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<?> criteriaQuery;
    private Root<Account> root;

    @BeforeEach
    void setUp() {
        criteriaBuilder = mock(CriteriaBuilder.class);
        criteriaQuery = mock(CriteriaQuery.class);
        root = mock(Root.class);
    }

    @Test
    void testWithFilter_withNonNullFilter() {
        AccountByFilterDto filter = mock(AccountByFilterDto.class);
        AccountSearchDto accountSearchDto = mock(AccountSearchDto.class);

        when(filter.getAccountSearchDto()).thenReturn(accountSearchDto);
        when(accountSearchDto.getIds()).thenReturn(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        when(accountSearchDto.getAuthor()).thenReturn("Иван");
        when(accountSearchDto.getFirstName()).thenReturn("Иван");
        when(accountSearchDto.getLastName()).thenReturn("Иванов");
        when(accountSearchDto.getCity()).thenReturn("Москва");
        when(accountSearchDto.getCountry()).thenReturn("Россия");
        when(accountSearchDto.getIsBlocked()).thenReturn(true);
        when(accountSearchDto.getIsDeleted()).thenReturn(false);
        when(accountSearchDto.getAgeFrom()).thenReturn(18);
        when(accountSearchDto.getAgeTo()).thenReturn(35);

        UUID currentUserId = UUID.randomUUID();

        Specification<Account> spec = AccountSpecification.withFilter(filter, currentUserId);

        assertNotNull(spec);
    }


    @Test
    void testByAccountIds_withEmptyList() {
        List<UUID> ids = List.of();
        Specification<Account> spec = AccountSpecification.byAccountIds(ids);

        Predicate mockPredicate = mock(Predicate.class);
        when(criteriaBuilder.conjunction()).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).conjunction();
    }

    @Test
    void testByAuthor_withEmptyAuthor() {
        String author = "";
        Specification<Account> spec = AccountSpecification.byAuthor(author);

        Predicate mockPredicate = mock(Predicate.class);
        when(criteriaBuilder.conjunction()).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).conjunction();
    }

    @Test
    void testByFirstName_withNonEmptyFirstName() {
        String firstName = "Иван";
        Specification<Account> spec = AccountSpecification.byFirstName(firstName);

        Predicate mockPredicate = mock(Predicate.class);
        Expression<String> lowerFirstName = mock(Expression.class);
        when(criteriaBuilder.lower(root.get(Account.Fields.firstName))).thenReturn(lowerFirstName);
        when(criteriaBuilder.like(lowerFirstName, "иван%")).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).like(lowerFirstName, "иван%");
    }

    @Test
    void testByFirstName_withEmptyFirstName() {
        String firstName = "";
        Specification<Account> spec = AccountSpecification.byFirstName(firstName);

        Predicate mockPredicate = mock(Predicate.class);
        when(criteriaBuilder.conjunction()).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).conjunction();
    }

    @Test
    void testByLastName_withNonEmptyLastName() {
        String lastName = "Иванов";
        Specification<Account> spec = AccountSpecification.byLastName(lastName);

        Predicate mockPredicate = mock(Predicate.class);
        Expression<String> lowerLastName = mock(Expression.class);
        when(criteriaBuilder.lower(root.get(Account.Fields.lastName))).thenReturn(lowerLastName);
        when(criteriaBuilder.like(lowerLastName, "иванов%")).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).like(lowerLastName, "иванов%");
    }

    @Test
    void testByLastName_withEmptyLastName() {
        String lastName = "";
        Specification<Account> spec = AccountSpecification.byLastName(lastName);

        Predicate mockPredicate = mock(Predicate.class);
        when(criteriaBuilder.conjunction()).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).conjunction();
    }
    @Test
    void testByAge() {
        Integer ageFrom = 18;
        Integer ageTo = 35;

        Specification<Account> ageSpec = AccountSpecification.byAge(ageFrom, ageTo);

        assertNotNull(ageSpec);
    }

    @Test
    void testByBlocked() {
        Boolean isBlocked = true;
        Specification<Account> blockedSpec = AccountSpecification.byBlocked(isBlocked);

        assertNotNull(blockedSpec);
    }

    @Test
    void testNotCurrentUser() {
        UUID currentUserId = UUID.randomUUID();

        Specification<Account> notCurrentUserSpec = AccountSpecification.notCurrentUser(currentUserId);

        assertNotNull(notCurrentUserSpec);
    }

    @Test
    void testByCity() {
        String city = "Москва";
        Specification<Account> citySpec = AccountSpecification.byCity(city);

        assertNotNull(citySpec);
    }

    @Test
    void testByCountry() {
        String country = "Россия";
        Specification<Account> countrySpec = AccountSpecification.byCountry(country);

        assertNotNull(countrySpec);
    }

    @Test
    void testByDeleted() {
        Boolean isDeleted = false;
        Specification<Account> deletedSpec = AccountSpecification.byDeleted(isDeleted);

        assertNotNull(deletedSpec);
    }
}