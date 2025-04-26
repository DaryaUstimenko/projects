package com.example.social_network_account.client;

import feign.Param;
import feign.RequestLine;

public interface AuthServiceClient {

    @RequestLine("GET /validate?token={token}")
    Boolean validateToken(@Param("token") String token);
}