package com.eleks.placescanner.place.service.precisely;

import com.eleks.placescanner.place.service.KafkaProducer;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicAdvancedRequest;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicRequest;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.plecescanner.common.domain.demographic.precisaly.PreciselyToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Base64;

public class PreciselyClient {
    private final String demographicByLocationURI;
    private final String demographicAdvanceURI;
    private final String oauthTokenURI;

    private final String preciselyApiKey;
    private final String preciselyApiSecret;

    private final RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);


    public PreciselyClient(String demographicByLocationURI, String demographicAdvanceURI, String oauthTokenURI, String preciselyApiKey, String preciselyApiSecret, RestTemplate restTemplate) {
        this.demographicByLocationURI = demographicByLocationURI;
        this.demographicAdvanceURI = demographicAdvanceURI;
        this.oauthTokenURI = oauthTokenURI;
        this.preciselyApiKey = preciselyApiKey;
        this.preciselyApiSecret = preciselyApiSecret;
        this.restTemplate = restTemplate;
    }

    public DemographicResponse callDemographicByLocation(DemographicRequest request) {
        try {
            var securityToken = getSecurityToken();
            var uri = UriComponentsBuilder.fromUriString(demographicByLocationURI + "?"
                    + "longitude=" + request.longitude() + "&"
                    + "latitude=" + request.latitude() + "&"
                    + "profile=" + request.profile() + "&"
                    + "filter=" + request.filter() + "&"
                    + "valueFormat=" + request.valueFormat() + "&"
                    + "variableLevel=" + request.variableLevel() + "&"
            ).build().toUri();
            var requestEntity = buildGetRequest(uri, securityToken);
            var type = new ParameterizedTypeReference<DemographicResponse>() {
            };
            return restTemplate.exchange(requestEntity, type).getBody();

        } catch (HttpServerErrorException e) {
            LOGGER.error("callDemographicByLocation exception " + e);
            throw e;
        }
    }

    public DemographicResponse callDemographicAdvance(DemographicAdvancedRequest request) {
        try {
            var securityToken = getSecurityToken();
            var uri = UriComponentsBuilder.fromUriString(demographicAdvanceURI).build().toUri();
            var requestEntity = buildPostRequest(uri, request, securityToken);
            var type = new ParameterizedTypeReference<DemographicResponse>() {
            };
            return restTemplate.
                    exchange(requestEntity, type).getBody();

        } catch (HttpServerErrorException e) {
            LOGGER.error("callDemographicByLocation exception " + e);
            throw e;
        }
    }

    public String getSecurityToken() {
        var creds = String.format("%s:%s", preciselyApiKey, preciselyApiSecret);
        var encodedCreds = "Basic  " + Base64.getEncoder().encodeToString(creds.getBytes());
        var requestEntity = buildPostRequest(
                UriComponentsBuilder.fromUriString(oauthTokenURI).build().toUri(),
                "grant_type=client_credentials",
                encodedCreds
        );

        var response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<PreciselyToken>() {
        }).getBody();
        if (response == null || response.accessToken() == null) {
            throw new IllegalStateException("no token");
        }
        return "Bearer " + response.accessToken();

    }

    private RequestEntity<Void> buildGetRequest(URI endpoint, String securityToken) {
        return RequestEntity
                .get(endpoint)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.AUTHORIZATION, securityToken)
                .build();
    }

    private RequestEntity<Object> buildPostRequest(URI endpoint, Object request, String securityToken) {
        return RequestEntity
                .post(endpoint)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.AUTHORIZATION, securityToken)
                .body(request);
    }
}
