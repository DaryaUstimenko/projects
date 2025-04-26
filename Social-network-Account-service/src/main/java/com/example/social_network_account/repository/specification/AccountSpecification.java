package com.example.social_network_account.repository.specification;

import com.example.social_network_account.entity.Account;
import com.example.social_network_account.dto.AccountByFilterDto;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AccountSpecification {
    static Specification<Account> withFilter(AccountByFilterDto filter, UUID currentUserId) {
        Specification<Account> spec = Specification.where(
                        byAccountIds(filter.getAccountSearchDto().getIds()))
                .and(byAuthor(filter.getAccountSearchDto().getAuthor()))
                .and(byFirstName(filter.getAccountSearchDto().getFirstName()))
                .and(byLastName(filter.getAccountSearchDto().getLastName()))
                .and(byCity(filter.getAccountSearchDto().getCity()))
                .and(byCountry(filter.getAccountSearchDto().getCountry()))
                .and(byBlocked(filter.getAccountSearchDto().getIsBlocked()))
                .and(byDeleted(filter.getAccountSearchDto().getIsDeleted()))
                .and(byAge(filter.getAccountSearchDto().getAgeFrom(), filter.getAccountSearchDto().getAgeTo()));
        if (currentUserId != null) {
            spec = spec.and(notCurrentUser(currentUserId));
        }
        return spec;
    }

    static Specification<Account> notCurrentUser(UUID currentUserId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get(Account.Fields.id), currentUserId);
    }

    static Specification<Account> byAccountIds(List<UUID> ids) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(ids)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.in(root.get(Account.Fields.id)).value(ids);
        };
    }

    static Specification<Account> byAuthor(String author) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(author)) {
                return criteriaBuilder.conjunction();
            }
            String lowerAuthor = author.toLowerCase();
            Expression<String> lowerFirstName = criteriaBuilder.lower(root.get(Account.Fields.firstName));
            Expression<String> lowerLastName = criteriaBuilder.lower(root.get(Account.Fields.lastName));
            Predicate firstNameLike = criteriaBuilder.like(lowerFirstName, lowerAuthor + "%");
            Predicate lastNameLike = criteriaBuilder.like(lowerLastName, lowerAuthor + "%");
            return criteriaBuilder.or(firstNameLike, lastNameLike);
        };
    }

    static Specification<Account> byFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(firstName)) {
                return criteriaBuilder.conjunction();
            }
            String lowerName = firstName.toLowerCase();
            Expression<String> lowerFirstName = criteriaBuilder.lower(root.get(Account.Fields.firstName));
            return criteriaBuilder.like(lowerFirstName, lowerName + "%");
        };
    }

    static Specification<Account> byLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(lastName)) {
                return criteriaBuilder.conjunction();
            }
            String lowerName = lastName.toLowerCase();
            Expression<String> lowerLastName = criteriaBuilder.lower(root.get(Account.Fields.lastName));
            return criteriaBuilder.like(lowerLastName, lowerName + "%");
        };
    }

    static Specification<Account> byAge(Integer ageFrom, Integer ageTo) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (ageFrom != null) {
                LocalDate dateTo = LocalDate.now().minusYears(ageFrom);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(
                        root.get(Account.Fields.birthDate), Date.from(dateTo.atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
            if (ageTo != null) {
                LocalDate dateFrom = LocalDate.now().minusYears(ageTo);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(
                        root.get(Account.Fields.birthDate), Date.from(dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
            return predicate;
        };
    }

    static Specification<Account> byCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(city)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get(Account.Fields.city), "%" + city + "%");
        };
    }

    static Specification<Account> byCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(country)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get(Account.Fields.country), "%" + country + "%");
        };
    }

    static Specification<Account> byBlocked(Boolean isBlocked) {
        return (root, query, criteriaBuilder) -> {
            if (isBlocked == null) {
                return criteriaBuilder.conjunction();
            }
            if (isBlocked) {
                return criteriaBuilder.equal(root.get(Account.Fields.isBlocked), true);
            }
            return criteriaBuilder.equal(root.get(Account.Fields.isBlocked), false);
        };
    }

    static Specification<Account> byDeleted(Boolean isDeleted) {
        return (root, query, criteriaBuilder) -> {
            if (isDeleted == null) {
                return criteriaBuilder.conjunction();
            }
            if (isDeleted) {
                return criteriaBuilder.equal(root.get(Account.Fields.isDeleted), true);
            }
            return criteriaBuilder.equal(root.get(Account.Fields.isDeleted), false);
        };
    }
}