package com.eleks.placescanner.place.config;

import com.eleks.placescanner.place.service.census.CensusClient;
import com.eleks.placescanner.place.service.nominatim.NominatimClient;
import com.eleks.placescanner.place.service.precisely.PreciselyClient;
import com.eleks.placescanner.place.service.promaptools.PromaptoolsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"com.eleks.placescanner.place", "com.eleks.plecescanner.common"})
@EnableScheduling
public class ScannerConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    @Primary
    public PreciselyClient preciselyClient(
            @Value("${url.api.precisely.demographic-by-location}") String demographicByLocationURI,
            @Value("${url.api.precisely.demographic-advance}") String demographicAdvanceURI,
            @Value("${url.api.precisely.crime-by-location}") String crimeByLocationURI,
            @Value("${url.api.precisely.oauth-token}") String oauthTokenURI,
            @Value("#{systemEnvironment['PRECISELY_API_KEY']}") String preciselyApiKey,
            @Value("#{systemEnvironment['PRECISELY_API_SECRET']}") String preciselyApiSecret,
            RestTemplate restTemplate) {
        return new PreciselyClient(demographicByLocationURI, demographicAdvanceURI, crimeByLocationURI, oauthTokenURI, preciselyApiKey, preciselyApiSecret, restTemplate);
    }

    @Bean
    @Primary
    public NominatimClient nominatimClient(
            @Value("${url.api.nominatim.place-polygon}") String placePolygonURI,
            RestTemplate restTemplate) {
        return new NominatimClient(placePolygonURI, restTemplate);
    }

    @Bean
    @Primary
    public CensusClient censusClient(
            @Value("${url.api.census.popclock-data}") String popclockDataURI,
            RestTemplate restTemplate) {
        return new CensusClient(popclockDataURI, restTemplate);
    }

    @Bean
    @Primary
    public PromaptoolsClient promaptoolsClient(
            @Value("${url.api.promaptools.get-lat-lng-by-zip}") String latLngByZipURI,
            RestTemplate restTemplate) {
        return new PromaptoolsClient(latLngByZipURI, restTemplate);
    }

    @Bean
    @Primary
    public RequestListener requestListener() {
        return new RequestListener();
    }
}
