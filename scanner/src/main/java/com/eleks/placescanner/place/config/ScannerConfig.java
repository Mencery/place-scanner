package com.eleks.placescanner.place.config;

import com.eleks.placescanner.place.service.census.CensusClient;
import com.eleks.placescanner.place.service.nominatim.NominatimClient;
import com.eleks.placescanner.place.service.precisely.PreciselyClient;
import com.eleks.placescanner.place.service.promaptools.PromaptoolsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class ScannerConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate(
    ) {
        return new RestTemplateBuilder().build();
    }

    @Bean
    @Primary
    public PreciselyClient preciselyClient(
            @Value("${precisely.api.demographic-by-location.url}") String demographicByLocationURI,
            @Value("${precisely.api.demographic-advance.url}") String demographicAdvanceURI,
            @Value("${precisely.api.crime-by-location.url}") String crimeByLocationURI,
            @Value("${precisely.api.oauth.token.url}") String oauthTokenURI,
            @Value("#{systemEnvironment['PRECISELY_API_KEY']}") String preciselyApiKey,
            @Value("#{systemEnvironment['PRECISELY_API_SECRET']}") String preciselyApiSecret,
            RestTemplate restTemplate

    ) {
        return new PreciselyClient(demographicByLocationURI, demographicAdvanceURI, crimeByLocationURI, oauthTokenURI, preciselyApiKey, preciselyApiSecret, restTemplate);
    }

    @Bean
    @Primary
    public NominatimClient nominatimClient(
            @Value("${nominatim.api.place-polygon.url}") String placePolygonURI,
            RestTemplate restTemplate
    ) {
        return new NominatimClient(placePolygonURI, restTemplate);
    }

    @Bean
    @Primary
    public CensusClient censusClient(
            @Value("${census.api.popclock-data.url}") String popclockDataURI,
            RestTemplate restTemplate
    ) {
        return new CensusClient(popclockDataURI, restTemplate);
    }

    @Bean
    @Primary
    public PromaptoolsClient promaptoolsClient(
            @Value("${promaptools.api.get-lat-lng-by-zip.url}") String latLngByZipURI,
            RestTemplate restTemplate
    ) {
        return new PromaptoolsClient(latLngByZipURI, restTemplate);
    }
}
