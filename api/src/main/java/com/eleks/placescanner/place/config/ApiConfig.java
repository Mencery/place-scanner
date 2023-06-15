package com.eleks.placescanner.place.config;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;


@Configuration
@ComponentScan(basePackages = {"com.eleks.placescanner.place", "com.eleks.plecescanner.dao", "com.eleks.plecescanner.common"})
@EnableMongoRepositories(basePackages = {"com.eleks.plecescanner.dao.repository"})
@EnableMongock
public class ApiConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate() {
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

    @Bean
    @Primary
    @Order(1)
    public RequestListener requestListener() {
        return new RequestListener();
    }
}
