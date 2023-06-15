package com.eleks.placescanner.place.config;

import com.eleks.plecescanner.common.security.GoogleTokenVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;

public class RequestListener extends OrderedRequestContextFilter {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Autowired
    GoogleTokenVerifier googleTokenVerifier;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        var token = request.getHeader("Authorization");

        if (request.getRequestURI().contains("validated") && googleTokenVerifier.isAuthorizationHeaderEmpty(token)) {
            throw new IllegalAccessException("/validated requests aren't secure");
        }

        if (googleTokenVerifier.isAuthorizationHeaderNotEmpty(token) && googleTokenVerifier.isTokenValid(token, clientId)) {
            request.getRequestDispatcher("/validated" + request.getRequestURI()).forward(request, response);
            return;
        }

        super.doFilterInternal(request, response, filterChain);
    }
}
