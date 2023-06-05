package com.eleks.placescanner.place.service.pollution;

import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.pollution.PollutionResponse;
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
    private static final Integer AIR_QUALITY_INDEX = 10;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PollutionService pollutionService;

    private ObjectMapper objectMapper;
    private PlaceRequest placeRequest;
    private PollutionResponse pollutionResponse;

    @BeforeEach
    public void setUp() {
        placeRequest = new PlaceRequest(CITY, STATE);
        pollutionResponse = new PollutionResponse(AIR_QUALITY_INDEX);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnPollutionResponse_whenCallGetPollution() throws Exception {
        when(pollutionService.getPollutionByPlace(CITY, STATE)).thenReturn(pollutionResponse);

        mockMvc.perform(post(POLLUTION_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest)))
                .andExpect(jsonPath("$.airQualityIndex").value(AIR_QUALITY_INDEX));
    }
}
