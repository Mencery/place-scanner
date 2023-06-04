package com.eleks.placescanner.place.service.scanner;

import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.crime.CrimeResponse;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.eleks.placescanner.place.service.scanner.ScannerURLs.CRIME_BY_LOCATION;
import static com.eleks.placescanner.place.service.scanner.ScannerURLs.DEMOGRAPHIC_ADVANCE;

public class ScannerClient {

    private final String scannerURI;
    private final RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(ScannerClient.class);

    public ScannerClient(String scannerURI, RestTemplate restTemplate) {
        this.scannerURI = scannerURI;
        this.restTemplate = restTemplate;
    }

    public DemographicResponse callDemographicAdvance(PlaceRequest request) {
        try {
            var securityToken = "";
            var uri = UriComponentsBuilder.fromUriString(scannerURI+DEMOGRAPHIC_ADVANCE).build().toUri();
            var requestEntity = buildPostRequest(uri, request, securityToken);
            var type = new ParameterizedTypeReference<DemographicResponse>() {
            };
            return restTemplate.exchange(requestEntity, type).getBody();

        } catch (HttpServerErrorException e) {
            LOGGER.error("callDemographicByLocation exception " + e);
            throw e;
        }
    }

    public CrimeResponse callCrimeByLocation(PlaceRequest request) {
        try {
            var securityToken = "";
            var uri = UriComponentsBuilder.fromUriString(scannerURI+CRIME_BY_LOCATION).build().toUri();
            var requestEntity = buildPostRequest(uri, request, securityToken);
            var type = new ParameterizedTypeReference<CrimeResponse>() {
            };
            return restTemplate.exchange(requestEntity, type).getBody();

        } catch (HttpServerErrorException e) {
            LOGGER.error("callCrimeByLocation exception " + e);
            throw e;
        }
    }

    private RequestEntity<Object> buildPostRequest(URI endpoint, Object request, String securityToken) {
        return RequestEntity
                .post(endpoint)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.AUTHORIZATION, securityToken)
                .body(request);
    }

}
