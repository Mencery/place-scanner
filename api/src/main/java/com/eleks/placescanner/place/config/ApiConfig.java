package com.eleks.placescanner.place.config;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ApiConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate(
    ) {
        return new RestTemplateBuilder().build();
    }

    @Bean
    @Primary
    public ScannerClient scannerClient(
            @Value("${scanner.url}") String scannerURI,
            RestTemplate restTemplate
    ) {
        return new ScannerClient(scannerURI, restTemplate);
    }
}
