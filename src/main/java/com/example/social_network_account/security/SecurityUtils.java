package com.example.social_network_account.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public  void saveToken(String token) {
        tokenHolder.set(token);
    }

    public  void removeToken() {
        tokenHolder.remove();
    }

    public  String getToken() {
        return tokenHolder.get();
    }

    public  UUID getAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object accountId = authentication.getPrincipal();
            if (accountId instanceof UUID) {
                return (UUID) accountId;
            }
        }
        throw new IllegalStateException("No authenticated user or invalid principal type");
    }
}