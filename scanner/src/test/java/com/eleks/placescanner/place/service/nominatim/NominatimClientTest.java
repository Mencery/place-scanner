package com.eleks.placescanner.place.service.nominatim;

import com.eleks.placescanner.common.domain.demographic.nominatim.GetPolygonRequest;
import com.eleks.placescanner.common.domain.demographic.nominatim.GetPolygonResponse;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class NominatimClientTest {

    private static final String NOMINATIM_POLYGON_RESPONSE_PATH = "./src/test/resources/json_objects/nominatimPolygonResponse.json";

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private NominatimClient nominatimClient;

    private ObjectMapper objectMapper;
    private ResponseEntity response;
    private GetPolygonRequest requestForWheeling;

    @BeforeEach
    void setUp() {
        requestForWheeling = new GetPolygonRequest(
                "Wheeling",
                "Illinois"
        );
        objectMapper = new ObjectMapper();
        response = mock(ResponseEntity.class);

        when(restTemplate.exchange(any(), eq(new ParameterizedTypeReference<List<GetPolygonResponse>>() {
        }))).thenReturn(response);
    }

    @Test
    void shouldReturnPlacePolygon() throws IOException {
        var file = new File(NOMINATIM_POLYGON_RESPONSE_PATH);
        List<GetPolygonResponse> expectedResponse = objectMapper.readValue(file, new TypeReference<List<GetPolygonResponse>>() {
        });

        when(response.getBody()).thenReturn(expectedResponse);

        var actualResponse = nominatimClient.callPlacePolygon(requestForWheeling);

        assertEquals(expectedResponse.get(0), actualResponse);
    }

    @Test
    void shouldReturnNotFound_whenEmptyResponse() {
        List<GetPolygonResponse> expectedResponse = new ArrayList<>();

        when(response.getBody()).thenReturn(expectedResponse);

        assertThrows(ResourceNotFoundException.class, () -> nominatimClient.callPlacePolygon(requestForWheeling));
    }

    @Test
    void shouldReturnUnexpectedResult_whenCensusReturnsServerErrorResponse() {
        when(restTemplate.exchange(any(), eq(new ParameterizedTypeReference<List<GetPolygonResponse>>() {
        }))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(UnexpectedResponseException.class, () -> nominatimClient.callPlacePolygon(requestForWheeling));
    }
}