package com.example.booking.aop;

import com.example.booking.entity.User;
import com.example.booking.exception.AccessDeniedException;
import com.example.booking.service.BookingService;
import com.example.booking.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationAspect {

    private final UserService userService;
    private final BookingService bookingService;

    @Before("@annotation(authorizeAction)")
    public void checkAuthorization(JoinPoint joinPoint, AuthorizeAction authorizeAction) {

        UUID entityId = (UUID) joinPoint.getArgs()[0];

        User user = extractUserFromEntity(entityId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!isAuthorized(user, authentication)) {

            log.info("Access denied. Users can only access their own information.");

            throw new AccessDeniedException("You are not authorized to " + authorizeAction.actionType() + " this item");

        }

    }

    private User extractUserFromEntity(UUID entityId) {

        return Stream.of(

                        attempt(() -> userService.findById(entityId)),
                        attempt(() -> bookingService.findById(entityId).getUser())

                ).filter(Optional::isPresent)

                .map(Optional::get)

                .findFirst()

                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

    }

    private Optional<User> attempt(Supplier<User> supplier) {

        try {

            return Optional.ofNullable(supplier.get());

        } catch (EntityNotFoundException e) {

            return Optional.empty();

        }

    }

    private boolean isAuthorized(User user, Authentication authentication) {

        if (authentication == null || authentication.getPrincipal() == null) {

            return false;

        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                user.getId().equals(userService.findByUsername(principal.getUsername()).getId());
    }
}