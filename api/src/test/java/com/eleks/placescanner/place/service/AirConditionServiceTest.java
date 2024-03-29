package com.eleks.placescanner.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.common.domain.pollution.PollutionResponse;
import com.eleks.placescanner.common.domain.pollution.weather.WeatherResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.eleks.placescanner.place.service.scanner.ScannerClient;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AirConditionServiceTest {

    private ScannerClient scannerClient;
    private AirConditionService airConditionService;

    @BeforeEach
    public void setUp() {
        scannerClient = mock(ScannerClient.class);
        airConditionService = new AirConditionService(scannerClient);
    }

    @Test
    void getAirInfo() throws ExecutionException, InterruptedException {
        var expected = new AirResponse(new PollutionResponse("Good"), new WeatherResponse(82));
        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();
        when(scannerClient.getAirInfo(placeRequest, "", errorMessages)).thenReturn(expected);

        var actual = airConditionService.getAirInfo(placeRequest, "", errorMessages).get();

        verify(scannerClient, times(1)).getAirInfo(placeRequest, "", errorMessages);
        assertEquals(expected, actual);
    }

    @Test
    void getAirInfo_WhenNotFound() throws ExecutionException, InterruptedException {
        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();
        when(scannerClient.getAirInfo(placeRequest, "", errorMessages)).thenReturn(null);

        var actual = airConditionService.getAirInfo(placeRequest, "", errorMessages).get();

        verify(scannerClient, times(1)).getAirInfo(placeRequest, "", errorMessages);
        assertNull(actual);
    }
}