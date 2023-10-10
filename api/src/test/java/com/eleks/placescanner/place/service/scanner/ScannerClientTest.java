package com.eleks.placescanner.place.service.scanner;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.crime.CrimeResponse;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScannerClientTest {

    private static final String DEMOGRAPHICS_ADVANCE_RESPONSE_PATH = "./src/test/resources/json_objects/demographicsSegmentationAdvancedResponse.json";
    private static final String IQ_AIR_RESPONSE_PATH = "./src/test/resources/json_objects/airIQResponse.json";
    private static final String CRIME_BY_LOCATION_RESPONSE_PATH = "./src/test/resources/json_objects/crimeByLocationResponse.json";

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ScannerClient scannerClient;

    private ObjectMapper objectMapper;
    private ResponseEntity response;
    private PlaceRequest placeRequest;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        response = mock(ResponseEntity.class);
        placeRequest = new PlaceRequest("testName", "testState", "testZip");
    }

    @Test
    void getDemographic() throws IOException {
        DemographicResponse expectedResponse = objectMapper.readValue(
                new File(DEMOGRAPHICS_ADVANCE_RESPONSE_PATH),
                DemographicResponse.class);

        when(response.getBody()).thenReturn(expectedResponse);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);

        DemographicResponse actualResponse = scannerClient.callDemographicAdvance(placeRequest, "", new ArrayList<>());
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getDemographic_WhenNotFoundError() throws IOException {
        var errorMessage = new ErrorMessage(404, null, "not found", "test");
        var errorMessagesList = new ArrayList<ErrorMessage>();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "test", objectMapper.writeValueAsBytes(errorMessage), null));

        var actualResponse = scannerClient.callDemographicAdvance(placeRequest, "", errorMessagesList);

        assertNull(actualResponse);
        assertNotNull(errorMessagesList.get(0));
        assertEquals(errorMessage, errorMessagesList.get(0));
    }

    @Test
    void getDemographic_WhenServerError() throws IOException {
        var errorMessage = new ErrorMessage(500, null, "internal server error", "test");
        var errorMessagesList = new ArrayList<ErrorMessage>();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "test", objectMapper.writeValueAsBytes(errorMessage), null));

        var actualResponse = scannerClient.callDemographicAdvance(placeRequest, "", errorMessagesList);

        assertNull(actualResponse);
        assertNotNull(errorMessagesList.get(0));
        assertEquals(errorMessage, errorMessagesList.get(0));
    }

    @Test
    void getAirInfo() throws IOException {
        AirResponse expectedResponse = objectMapper.readValue(
                new File(IQ_AIR_RESPONSE_PATH),
                AirResponse.class);

        when(response.getBody()).thenReturn(expectedResponse);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);

        AirResponse actualResponse = scannerClient.getAirInfo(placeRequest, "", new ArrayList<>());
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getAirInfo_WhenNotFoundError() throws IOException {
        var errorMessage = new ErrorMessage(404, null, "not found", "test");
        var errorMessagesList = new ArrayList<ErrorMessage>();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "test", objectMapper.writeValueAsBytes(errorMessage), null));

        var actualResponse = scannerClient.getAirInfo(placeRequest, "", errorMessagesList);

        assertNull(actualResponse);
        assertNotNull(errorMessagesList.get(0));
        assertEquals(errorMessage, errorMessagesList.get(0));
    }

    @Test
    void getAirInfo_WhenServerError() throws IOException {
        var errorMessage = new ErrorMessage(500, null, "internal server error", "test");
        var errorMessagesList = new ArrayList<ErrorMessage>();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "test", objectMapper.writeValueAsBytes(errorMessage), null));

        var actualResponse = scannerClient.getAirInfo(placeRequest, "", errorMessagesList);

        assertNull(actualResponse);
        assertNotNull(errorMessagesList.get(0));
        assertEquals(errorMessage, errorMessagesList.get(0));
    }

    @Test
    void getCrime() throws IOException {
        CrimeResponse expectedResponse = objectMapper.readValue(
                new File(CRIME_BY_LOCATION_RESPONSE_PATH),
                CrimeResponse.class);

        when(response.getBody()).thenReturn(expectedResponse);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);

        CrimeResponse actualResponse = scannerClient.callCrimeByLocation(placeRequest, "", new ArrayList<>());
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getCrime_WhenNotFoundError() throws IOException {
        var errorMessage = new ErrorMessage(404, null, "not found", "test");
        var errorMessagesList = new ArrayList<ErrorMessage>();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "test", objectMapper.writeValueAsBytes(errorMessage), null));

        var actualResponse = scannerClient.callCrimeByLocation(placeRequest, "", errorMessagesList);

        assertNull(actualResponse);
        assertNotNull(errorMessagesList.get(0));
        assertEquals(errorMessage, errorMessagesList.get(0));
    }

    @Test
    void getCrime_WhenServerError() throws IOException {
        var errorMessage = new ErrorMessage(500, null, "internal server error", "test");
        var errorMessagesList = new ArrayList<ErrorMessage>();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "test", objectMapper.writeValueAsBytes(errorMessage), null));

        var actualResponse = scannerClient.callCrimeByLocation(placeRequest, "", errorMessagesList);

        assertNull(actualResponse);
        assertNotNull(errorMessagesList.get(0));
        assertEquals(errorMessage, errorMessagesList.get(0));
    }
}