package com.eleks.placescanner.common.security.filter;

import com.eleks.placescanner.common.security.GoogleTokenUtil;
import com.eleks.placescanner.common.security.GoogleTokenVerifier;
import com.eleks.placescanner.common.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authorization = request.getHeader("Authorization");
        var principal = request.getUserPrincipal();
        var token = googleTokenUtil.getToken(authorization, principal);

        if (token != null && googleTokenVerifier.isTokenValid(token, clientId)) {
            var email = googleTokenUtil.getEmail(token);
            var userDetails = customUserDetailsService.loadUserByUsername(email);
            var authorities = userDetails.getAuthorities();
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
