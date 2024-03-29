package com.eleks.placescanner.place.service.promaptools;

import static com.eleks.placescanner.place.Util.JSON_OBJECTS_FOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.eleks.placescanner.common.domain.promaptools.PromaptoolsResponse;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
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
class PromaptoolsClientTest {

    private static final String LATITUDE_LONGITUDE_FOR_60090_RESPONSE_PATH
            = JSON_OBJECTS_FOLDER + "promaptoolZip60090Response.json";

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private PromaptoolsClient promaptoolsClient;

    private ObjectMapper objectMapper;
    private ResponseEntity response;
    private ParameterizedTypeReference<PromaptoolsResponse> type;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        response = mock(ResponseEntity.class);
        type = new ParameterizedTypeReference<PromaptoolsResponse>() {
        };
        when(restTemplate.exchange(any(), eq(type))).thenReturn(response);
    }

    @Test
    void shouldReturnUsPopulation() throws IOException {
        PromaptoolsResponse expectedResponse = objectMapper.readValue(
                new File(LATITUDE_LONGITUDE_FOR_60090_RESPONSE_PATH),
                PromaptoolsResponse.class);

        when(response.getBody()).thenReturn(expectedResponse);

        var actualResponse = promaptoolsClient.callLatLngByZip("60090");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getNotFoundWhenEmptyResponse() {
        PromaptoolsResponse expectedResponse = new PromaptoolsResponse(0, null);

        when(response.getBody()).thenReturn(expectedResponse);

        assertThrows(ResourceNotFoundException.class, () -> promaptoolsClient.callLatLngByZip("60090"));
    }

    @Test
    void getUnexpectedResultWhenCensusReturnsServerErrorResponse() {
        when(restTemplate.exchange(any(), eq(type)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(UnexpectedResponseException.class, () -> promaptoolsClient.callLatLngByZip("60090"));
    }
}