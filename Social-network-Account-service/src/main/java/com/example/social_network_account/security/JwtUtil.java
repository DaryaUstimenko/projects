package com.example.social_network_account.security;

import org.json.JSONObject;

import java.util.Base64;
import java.util.List;
import java.util.UUID;


public class JwtUtil {

    private JwtUtil() {}


    public static UUID extractAccountId(String token) {
        JSONObject json = extractPayload(token);


        String accountIdStr = json.optString("accountId", "");

        if (accountIdStr.isEmpty()) {
            throw new IllegalArgumentException("Invalid JWT token: missing or empty 'accountId' field");
        }

        return UUID.fromString(accountIdStr);
    }

    public static List<String> extractRoles(String token) {
        JSONObject json = extractPayload(token);


        return json.optJSONArray("roles") != null
                ? json.optJSONArray("roles").toList().stream()
                .map(Object::toString)
                .toList()
                : List.of();
    }

    private static JSONObject extractPayload(String token) {
        String[] chunks = token.split("\\.");
        if (chunks.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token: incorrect format");
        }

        try {
            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            return new JSONObject(payload);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decode JWT token", e);
        }
    }
}