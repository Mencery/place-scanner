package com.eleks.plecescanner.common.security.filter;

import com.eleks.plecescanner.common.security.GoogleTokenUtil;
import com.eleks.plecescanner.common.security.GoogleTokenVerifier;
import com.eleks.plecescanner.common.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private GoogleTokenUtil googleTokenUtil;

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorization = request.getHeader("Authorization");
        var principal = request.getUserPrincipal();
        var token = googleTokenUtil.getToken(authorization, principal);

        if (token != null && googleTokenVerifier.isTokenValid(token, clientId)) {
            var email = googleTokenUtil.getEmail(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
