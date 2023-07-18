package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.place.service.pollution.PollutionService;
import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.common.domain.pollution.PollutionResponse;
import com.eleks.placescanner.common.domain.pollution.weather.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PollutionControllerTest {

    private static final String POLLUTION_URL = "/pollution";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String ZIPCODE = "zipcode";
    private static final String AIR_QUALITY = "Air quality";
    private static final Integer TEMPERATURE = 10;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PollutionService pollutionService;

    private ObjectMapper objectMapper;
    private PlaceRequest placeRequest;
    private PollutionResponse pollutionResponse;
    private WeatherResponse weatherResponse;
    private AirResponse airResponse;

    @BeforeEach
    public void setUp() {
        placeRequest = new PlaceRequest(CITY, STATE, ZIPCODE);
        pollutionResponse = new PollutionResponse(AIR_QUALITY);
        weatherResponse = new WeatherResponse(TEMPERATURE);
        airResponse = new AirResponse(pollutionResponse, weatherResponse);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnPollutionResponse_whenCallGetPollution() throws Exception {
        when(pollutionService.getPollutionByPlace(ZIPCODE)).thenReturn(airResponse);

        mockMvc.perform(post(POLLUTION_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest)))
                .andExpect(jsonPath("$.pollution.airQuality").value(AIR_QUALITY))
                .andExpect(jsonPath("$.weather.temperature").value(TEMPERATURE));
    }
}
