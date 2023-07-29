package com.eleks.placescanner.place.service.census;

import com.eleks.placescanner.common.domain.population.CensusResponse;
import com.eleks.placescanner.common.domain.population.PopClockResponse;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.place.service.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CensusClient {

    private final String popclockDataUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);


    public CensusClient(String popclockDataUrl, RestTemplate restTemplate) {
        this.popclockDataUrl = popclockDataUrl;
        this.restTemplate = restTemplate;
        objectMapper = new ObjectMapper();
    }

    public PopClockResponse callPopulationByClock() {
        try {

            var uri = UriComponentsBuilder.fromUriString(
                    popclockDataUrl + "?"
                            + "_=" + System.currentTimeMillis()
            ).build().toUri();
            var requestEntity = buildGetRequest(uri);

            var textResponse = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<String>() {
            }).getBody();
            var censusResponse = objectMapper.readValue(textResponse, CensusResponse.class);
            checkUsNotNull(censusResponse);
            return censusResponse.us();

        } catch (JsonProcessingException e) {
            throw new UnexpectedResponseException("Cannot parse census popclock response");
        } catch (HttpServerErrorException e) {
            LOGGER.error("Census popclock exception " + e);
            throw new UnexpectedResponseException(e.getMessage());
        }
    }

    private RequestEntity<Void> buildGetRequest(URI endpoint) {
        return RequestEntity
                .get(endpoint)
                .build();
    }

    private void checkUsNotNull(CensusResponse censusResponse) {
        if (censusResponse.us() == null) {
            throw new ResourceNotFoundException("No us population was found");
        }
    }
}
