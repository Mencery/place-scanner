package com.eleks.placescanner.place.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;

public class RequestListener extends OrderedRequestContextFilter {

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        super.doFilterInternal(request, response, filterChain);
    }
}
