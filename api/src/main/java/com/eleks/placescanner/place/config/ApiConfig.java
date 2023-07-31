package com.eleks.placescanner.place.config;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"com.eleks.placescanner.place",
        "com/eleks/placescanner/dao",
        "com.eleks.placescanner.common"})
@EnableMongoRepositories(basePackages = {"com.eleks.placescanner.dao.repository"})
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
            @Value("${url.api.place-scanner.scanner}") String scannerUrl,
            RestTemplate restTemplate) {
        return new ScannerClient(scannerUrl, restTemplate);
    }

    @Bean
    @Primary
    @Order(1)
    public RequestListener requestListener() {
        return new RequestListener();
    }
}
