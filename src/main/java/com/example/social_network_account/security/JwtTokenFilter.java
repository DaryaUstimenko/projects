package com.example.social_network_account.security;

import com.example.social_network_account.client.AuthServiceClient;
import com.example.social_network_account.exception.JwtAuthenticationException;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        StringBuffer requestURL = request.getRequestURL();
        String url = requestURL.toString();
        log.warn("JwtTokenFilter in request URL: {}", url);

        try {
            String jwtToken = getToken(request);
            boolean isTokenValid = authServiceClient.validateToken(jwtToken);

            if (!isTokenValid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            log.debug("isTokenValid: " + isTokenValid);
            List<String> roles = JwtUtil.extractRoles(jwtToken);
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UUID accountId = JwtUtil.extractAccountId(jwtToken);
            log.debug("accountId: " + accountId);

            Authentication authentication = new HeaderAuthenticationToken(accountId, null, authorities);
            log.debug("Authentication: " + authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (FeignException.FeignClientException | JwtAuthenticationException e) {
            log.error("Authentication error: {}", e.getMessage(), e);
            throw new NotAuthorizedException(response);
        }
    }

    private String getToken(HttpServletRequest request) throws JwtAuthenticationException {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            log.info("HeaderAuth: " + headerAuth);
            return headerAuth.substring(7);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT token is missing");
        }
    }
}
