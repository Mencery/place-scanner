package com.eleks.placescanner.place.service.promaptools;

import com.eleks.placescanner.place.service.KafkaProducer;
import com.eleks.placescanner.common.domain.promaptools.PromaptoolsResponse;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class PromaptoolsClient {

    private final String latLngByZipURI;
    private final RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    public PromaptoolsClient(String latLngByZipURI, RestTemplate restTemplate) {
        this.latLngByZipURI = latLngByZipURI;
        this.restTemplate = restTemplate;
    }

    public PromaptoolsResponse callLatLngByZip(String zipCode) {
        try {

            var uri = UriComponentsBuilder.fromUriString(
                    latLngByZipURI + "?"
                            + "zip=" + zipCode + "&key=17o8dysaCDrgvlc"
            ).build().toUri();
            var requestEntity = buildGetRequest(uri);
            var type = new ParameterizedTypeReference<PromaptoolsResponse>() {
            };
            var response = restTemplate.exchange(requestEntity, type).getBody();
            checkResponseNotNull(response);
            return response;

        } catch (HttpServerErrorException e) {
            LOGGER.error("Promaptools call exception " + e);
            throw new UnexpectedResponseException(e.getMessage());
        }
    }

    private RequestEntity<Void> buildGetRequest(URI endpoint) {
        return RequestEntity
                .get(endpoint)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    private void checkResponseNotNull(PromaptoolsResponse promaptoolsResponse) {
        if (promaptoolsResponse.outputList() == null || promaptoolsResponse.outputList().isEmpty()) {
            throw new ResourceNotFoundException("No us population was found");
        }
    }
}
