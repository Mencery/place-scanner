package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.place.service.precisely.PreciselyClient;
import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.crime.CrimeResponse;
import com.eleks.placescanner.common.domain.promaptools.PromaptoolsResponse;
import com.eleks.placescanner.common.exception.ControllerExceptionHandler;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CrimeControllerTest {

    private static final String CRIME_BY_LOCATION_URL = "/crime-by-location";
    private static final String CRIME_BY_LOCATION_RESPONSE_PATH = "./src/test/resources/json_objects/crimeByLocationResponse.json";
    private static final String LATITUDE_LONGITUDE_FOR_60090_RESPONSE_PATH = "./src/test/resources/json_objects/promaptoolZip60090Response.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrimeController controller;

    @SpyBean
    private PreciselyClient preciselyClient;

    @MockBean
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;
    private ResponseEntity crimeResponse;
    private ResponseEntity promaptoolsResponse;
    private ParameterizedTypeReference<CrimeResponse> crimeResponseType;
    private ParameterizedTypeReference<PromaptoolsResponse> promaptoolsResponseType;
    private PlaceRequest placeRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        crimeResponse = mock(ResponseEntity.class);
        promaptoolsResponse = mock(ResponseEntity.class);
        crimeResponseType = new ParameterizedTypeReference<>() {
        };
        promaptoolsResponseType = new ParameterizedTypeReference<>() {
        };

        placeRequest = new PlaceRequest("Wheeling", "Illinois", "60090");

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setViewResolvers(viewResolver)
                .build();

        doReturn("token").when(preciselyClient).getSecurityToken();

        when(restTemplate.exchange(any(), eq(crimeResponseType))).thenReturn(crimeResponse);
        when(restTemplate.exchange(any(), eq(promaptoolsResponseType))).thenReturn(promaptoolsResponse);
        when(crimeResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
    }

    @Test
    void shouldReturnCrimeResponse() throws Exception {
        CrimeResponse expectedCrimeResponse = objectMapper.readValue(
                new File(CRIME_BY_LOCATION_RESPONSE_PATH),
                CrimeResponse.class);
        PromaptoolsResponse expectedPromaptoolsResponse = objectMapper.readValue(
                new File(LATITUDE_LONGITUDE_FOR_60090_RESPONSE_PATH),
                PromaptoolsResponse.class);

        when(promaptoolsResponse.getBody()).thenReturn(expectedPromaptoolsResponse);
        when(crimeResponse.getBody()).thenReturn(expectedCrimeResponse);

        mockMvc.perform(post(CRIME_BY_LOCATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCrimeResponse)));
    }

    @Test
    void shouldReturnResourceNotFoundException_whenPreciselyReturnsEmpty() throws Exception {
        PromaptoolsResponse expectedPromaptoolsResponse = objectMapper.readValue(
                new File(LATITUDE_LONGITUDE_FOR_60090_RESPONSE_PATH),
                PromaptoolsResponse.class);

        when(promaptoolsResponse.getBody()).thenReturn(expectedPromaptoolsResponse);
        when(crimeResponse.getBody()).thenThrow(new ResourceNotFoundException("test"));

        mockMvc.perform(post(CRIME_BY_LOCATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void shouldReturnResourceNotFoundException_whenPromaptoolsReturnsException() throws Exception {
        CrimeResponse expectedCrimeResponse = objectMapper.readValue(
                new File(CRIME_BY_LOCATION_RESPONSE_PATH),
                CrimeResponse.class);

        when(promaptoolsResponse.getBody()).thenThrow(new HttpServerErrorException(HttpStatusCode.valueOf(500)));
        when(crimeResponse.getBody()).thenReturn(expectedCrimeResponse);

        mockMvc.perform(post(CRIME_BY_LOCATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnexpectedResponseException))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("500 INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.description").value("uri=" + CRIME_BY_LOCATION_URL));
    }
}