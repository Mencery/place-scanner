package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.domain.Crime;
import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.crime.CrimeResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrimeServiceTest {

    private static final String CRIME_BY_LOCATION_RESPONSE_PATH = "./src/test/resources/json_objects/crimeByLocationResponse.json";

    private ScannerClient scannerClient;
    private CrimeService crimeService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        scannerClient = mock(ScannerClient.class);
        crimeService = new CrimeService(scannerClient);
        objectMapper = new ObjectMapper();
    }

    @Test
    void getPlaceCrime() throws IOException, ExecutionException, InterruptedException {
        var response = objectMapper.readValue(
                new File(CRIME_BY_LOCATION_RESPONSE_PATH),
                CrimeResponse.class);
        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();

        when(scannerClient.callCrimeByLocation(placeRequest, "", errorMessages)).thenReturn(response);

        var indexVariables = response.themes().get(0).crimeIndexTheme().indexVariable();
        var expected = new Crime(indexVariables);
        var actual = crimeService.getPlaceCrime(placeRequest, "", errorMessages).get();

        verify(scannerClient, times(1)).callCrimeByLocation(placeRequest, "", errorMessages);
        assertEquals(expected, actual);
    }

    @Test
    void getPlaceCrime_WhenNotFound() throws ExecutionException, InterruptedException {
        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();

        when(scannerClient.getAirInfo(placeRequest, "", errorMessages)).thenReturn(null);

        var actual = crimeService.getPlaceCrime(placeRequest, "", errorMessages).get();

        verify(scannerClient, times(1)).callCrimeByLocation(placeRequest, "", errorMessages);
        assertNull(actual);
    }

    @Test
    void getPlaceCrime_WhenNoThemes(){
        var response = new CrimeResponse(new ArrayList<>());
        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();

        when(scannerClient.callCrimeByLocation(placeRequest, "", errorMessages)).thenReturn(response);

        assertThrows(CompletionException.class,
                () -> crimeService.getPlaceCrime(placeRequest, "", errorMessages).join(),
                "crime shoud have one default theme, theme size: 0");
    }

    @Test
    void getPlaceCrime_WhenMoreThanOneThemes() {
        var themesMock = mock( ArrayList.class);
        var response = new CrimeResponse(themesMock);
        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();

        when(themesMock.size()).thenReturn(2);
        when(scannerClient.callCrimeByLocation(placeRequest, "", errorMessages)).thenReturn(response);

        assertThrows(CompletionException.class,
                () -> crimeService.getPlaceCrime(placeRequest, "", errorMessages).join(),
                "crime shoud have one default theme, theme size: 2");
    }
}