package com.eleks.placescanner.place.config;

import com.eleks.plecescanner.common.security.GoogleTokenVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;

import java.io.IOException;

public class RequestListener extends OrderedRequestContextFilter {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Autowired
    GoogleTokenVerifier googleTokenVerifier;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = request.getHeader("Authorization");
        if (googleTokenVerifier.isAuthorizationHeaderNotEmpty(token) && googleTokenVerifier.isTokenValid(token, clientId)) {
            super.doFilterInternal(request, response, filterChain);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
