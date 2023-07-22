package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.Themes;
import com.eleks.placescanner.common.domain.demographic.precisaly.theme.Theme;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
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
import static org.mockito.Mockito.times;

class DemographicServiceTest {

    private static final String DEMOGRAPHICS_ADVANCE_RESPONSE_PATH = "./src/test/resources/json_objects/demographicsSegmentationAdvancedResponse.json";

    private ScannerClient scannerClient;
    private DemographicService demographicService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        scannerClient = mock(ScannerClient.class);
        demographicService = new DemographicService(scannerClient);
        objectMapper = new ObjectMapper();
    }

    @Test
    void getDemographicInfo() throws IOException, ExecutionException, InterruptedException {
        var response = objectMapper.readValue(
                new File(DEMOGRAPHICS_ADVANCE_RESPONSE_PATH),
                DemographicResponse.class);
        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();

        when(scannerClient.callDemographicAdvance(placeRequest, "", errorMessages)).thenReturn(response);

        var demographic = demographicService.getDemographicInfo(placeRequest, "", errorMessages).get();

        verify(scannerClient, times(1)).callDemographicAdvance(placeRequest, "", errorMessages);
        assertNotNull(demographic);
        assertNotNull(demographic.race());
        assertNotNull(demographic.income());
        assertNotNull(demographic.housing());
        assertNotNull(demographic.vehicle());
        assertEquals("3392", demographic.totalPlacePopulation());
    }

    @Test
    void getDemographicInfo_WhenNotFound() throws ExecutionException, InterruptedException {
        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();

        when(scannerClient.callDemographicAdvance(placeRequest, "", errorMessages)).thenReturn(null);

        var demographic = demographicService.getDemographicInfo(placeRequest, "", errorMessages).get();

        verify(scannerClient, times(1)).callDemographicAdvance(placeRequest, "", errorMessages);
        assertNull(demographic);
    }

    @Test
    void getDemographicInfo_WheNoThemeFound() {
        var populationTheme = new Theme(new ArrayList<>(), new ArrayList<>());
        var themes = new Themes(populationTheme, null, null, null,null,null );
        var response = new DemographicResponse(null, themes);

        var placeRequest = new PlaceRequest("testName", "testState", "testZip");
        var errorMessages = new ArrayList<ErrorMessage>();

        when(scannerClient.callDemographicAdvance(placeRequest, "", errorMessages)).thenReturn(response);

        assertThrows(CompletionException.class,
                () -> demographicService.getDemographicInfo(placeRequest, "", errorMessages).join(),
                "incorrect demographicInfo response");
    }
}