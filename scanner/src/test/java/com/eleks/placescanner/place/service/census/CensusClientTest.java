package com.eleks.placescanner.place.service.census;

import static com.eleks.placescanner.place.Util.JSON_OBJECTS_FOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.eleks.placescanner.common.domain.population.CensusResponse;
import com.eleks.placescanner.common.domain.population.PopClockResponse;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class CensusClientTest {

    private static final String CENSUS_US_POPULATION_PATH = JSON_OBJECTS_FOLDER + "censusUsPopulation.json";

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CensusClient censusClient;


    private ObjectMapper objectMapper;
    private ResponseEntity response;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        response = mock(ResponseEntity.class);

        when(restTemplate.exchange(any(), eq(new ParameterizedTypeReference<String>() {
        }))).thenReturn(response);
    }

    @Test
    void shouldReturnUsPopulation() throws IOException {
        var file = new File(CENSUS_US_POPULATION_PATH);
        var textResponse = new Scanner(file).nextLine();
        PopClockResponse expectedResponse = objectMapper.readValue(textResponse, CensusResponse.class).us();

        when(response.getBody()).thenReturn(textResponse);

        var actualResponse = censusClient.callPopulationByClock();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldReturnUnexpectedResponseException_whenEmptyResponse() {
        var textResponse = "";

        when(response.getBody()).thenReturn(textResponse);

        assertThrows(UnexpectedResponseException.class, () -> censusClient.callPopulationByClock());
    }

    @Test
    void getNotFoundWhenEmptyResponse() {
        var textResponse = "{}";

        when(response.getBody()).thenReturn(textResponse);

        assertThrows(ResourceNotFoundException.class, () -> censusClient.callPopulationByClock());
    }

    @Test
    void getUnexpectedResultWhenCensusReturnsServerErrorResponse() {
        when(restTemplate.exchange(any(), eq(new ParameterizedTypeReference<String>() {
        }))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(UnexpectedResponseException.class, () -> censusClient.callPopulationByClock());
    }
}