package com.eleks.placescanner.place.service.precisely;

import com.eleks.placescanner.common.domain.crime.CrimeRequest;
import com.eleks.placescanner.common.domain.crime.CrimeResponse;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicAdvancedRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.placescanner.common.domain.demographic.precisaly.PreciselyToken;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.place.service.KafkaProducer;
import java.net.URI;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class PreciselyClient {

    private final String demographicByLocationUrl;
    private final String demographicAdvanceUrl;
    private final String crimeByLocationUrl;
    private final String oauthTokenUrl;
    private final String preciselyApiKey;
    private final String preciselyApiSecret;
    private final RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    public PreciselyClient(String demographicByLocationUrl,
                           String demographicAdvanceUrl,
                           String crimeByLocationUrl,
                           String oauthTokenUrl,
                           String preciselyApiKey,
                           String preciselyApiSecret,
                           RestTemplate restTemplate) {
        this.demographicByLocationUrl = demographicByLocationUrl;
        this.demographicAdvanceUrl = demographicAdvanceUrl;
        this.crimeByLocationUrl = crimeByLocationUrl;
        this.oauthTokenUrl = oauthTokenUrl;
        this.preciselyApiKey = preciselyApiKey;
        this.preciselyApiSecret = preciselyApiSecret;
        this.restTemplate = restTemplate;
    }

    public DemographicResponse callDemographicByLocation(DemographicRequest request) {
        try {
            var securityToken = getSecurityToken();
            var uri = UriComponentsBuilder.fromUriString(demographicByLocationUrl + "?"
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
            var response = restTemplate.exchange(requestEntity, type);
            checkStatusCode(response);
            return restTemplate.exchange(requestEntity, type).getBody();

        } catch (NullPointerException e) {
            throw new ResourceNotFoundException("callDemographicByLocation exception returns empty body");
        } catch (HttpServerErrorException e) {
            LOGGER.error("callDemographicByLocation exception " + e);
            throw new UnexpectedResponseException(e.getMessage());
        }
    }

    public DemographicResponse callDemographicAdvance(DemographicAdvancedRequest request) {
        try {
            var securityToken = getSecurityToken();
            var uri = UriComponentsBuilder.fromUriString(demographicAdvanceUrl).build().toUri();
            var requestEntity = buildPostRequest(uri, request, securityToken);
            var type = new ParameterizedTypeReference<DemographicResponse>() {
            };

            var response = restTemplate.exchange(requestEntity, type);
            checkStatusCode(response);
            return restTemplate.exchange(requestEntity, type).getBody();
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException("callDemographicAdvance exception returns empty body");
        } catch (HttpServerErrorException e) {
            LOGGER.error("callDemographicAdvance exception " + e);
            throw new UnexpectedResponseException(e.getMessage());
        }
    }

    public CrimeResponse callCrimeByLocation(CrimeRequest request) {
        try {
            var securityToken = getSecurityToken();
            var uri = UriComponentsBuilder.fromUriString(crimeByLocationUrl + "?"
                    + "longitude=" + request.longitude() + "&"
                    + "latitude=" + request.latitude() + "&"
                    + "type=" + request.type() + "&"
                    + "includeGeometry=" + request.includeGeometry()
            ).build().toUri();
            var requestEntity = buildGetRequest(uri, securityToken);
            var type = new ParameterizedTypeReference<CrimeResponse>() {
            };
            var response = restTemplate.exchange(requestEntity, type);
            checkStatusCode(response);
            return restTemplate.exchange(requestEntity, type).getBody();
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException("callCrimeByLocation exception returns empty body");
        } catch (HttpServerErrorException e) {
            LOGGER.error("callCrimeByLocation exception " + e);
            throw new UnexpectedResponseException(e.getMessage());
        }
    }

    public String getSecurityToken() {
        var creds = String.format("%s:%s", preciselyApiKey, preciselyApiSecret);
        var encodedCreds = "Basic  " + Base64.getEncoder().encodeToString(creds.getBytes());
        var requestEntity = buildPostRequest(
                UriComponentsBuilder.fromUriString(oauthTokenUrl).build().toUri(),
                "grant_type=client_credentials",
                encodedCreds
        );

        var response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<PreciselyToken>() {
        }).getBody();
        if (response == null || response.accessToken() == null || response.accessToken().isEmpty()) {
            throw new ResourceNotFoundException("no token");
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

    private <T> void checkStatusCode(ResponseEntity<T> response) {
        if (response.getStatusCode().isError()) {
            throw new HttpServerErrorException(response.getStatusCode());
        }
    }
}
