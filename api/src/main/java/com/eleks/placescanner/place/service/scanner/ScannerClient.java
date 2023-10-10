package com.eleks.placescanner.place.service.scanner;

import static com.eleks.placescanner.place.service.scanner.ScannerUrls.CRIME_BY_LOCATION;
import static com.eleks.placescanner.place.service.scanner.ScannerUrls.DEMOGRAPHIC_ADVANCE;
import static com.eleks.placescanner.place.service.scanner.ScannerUrls.POLLUTION;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.crime.CrimeResponse;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class ScannerClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScannerClient.class);

    private final String scannerUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public ScannerClient(String scannerUrl, RestTemplate restTemplate) {
        this.scannerUrl = scannerUrl;
        this.restTemplate = restTemplate;
        objectMapper = new ObjectMapper();
    }

    public DemographicResponse callDemographicAdvance(PlaceRequest request,
                                                      String securityToken,
                                                      List<ErrorMessage> errorMessages) {
        try {
            var uri = UriComponentsBuilder.fromUriString(scannerUrl + DEMOGRAPHIC_ADVANCE).build().toUri();
            var requestEntity = buildPostRequest(uri, request, securityToken);
            var type = new ParameterizedTypeReference<DemographicResponse>() {
            };
            return restTemplate.exchange(requestEntity, type).getBody();

        } catch (HttpStatusCodeException e) {
            LOGGER.error("callDemographicByLocation exception " + e);
            addErrorMessage(errorMessages, e);
            return null;
        }
    }

    public AirResponse getAirInfo(PlaceRequest request, String securityToken, List<ErrorMessage> errorMessages) {
        try {
            var uri = UriComponentsBuilder.fromUriString(scannerUrl + POLLUTION).build().toUri();
            var requestEntity = buildPostRequest(uri, request, securityToken);
            var type = new ParameterizedTypeReference<AirResponse>() {
            };
            return restTemplate.exchange(requestEntity, type).getBody();

        } catch (HttpStatusCodeException e) {
            LOGGER.error("getAirInfo exception " + e);
            addErrorMessage(errorMessages, e);
            return null;
        }
    }


    public CrimeResponse callCrimeByLocation(PlaceRequest request,
                                             String securityToken,
                                             List<ErrorMessage> errorMessages) {
        try {
            var uri = UriComponentsBuilder.fromUriString(scannerUrl + CRIME_BY_LOCATION).build().toUri();
            var requestEntity = buildPostRequest(uri, request, securityToken);
            var type = new ParameterizedTypeReference<CrimeResponse>() {
            };
            return restTemplate.exchange(requestEntity, type).getBody();
        } catch (HttpStatusCodeException e) {
            LOGGER.error("callCrimeByLocation exception " + e);
            addErrorMessage(errorMessages, e);
            return null;
        }
    }

    private RequestEntity<Object> buildPostRequest(URI endpoint, PlaceRequest request, String securityToken) {
        return RequestEntity
                .post(endpoint)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.AUTHORIZATION, securityToken)
                .body(request);
    }

    @SneakyThrows
    private void addErrorMessage(List<ErrorMessage> errorMessages, HttpStatusCodeException e) {
        var errorMessage = objectMapper.readValue(e.getResponseBodyAsString(), ErrorMessage.class);
        errorMessages.add(errorMessage);
    }
}
