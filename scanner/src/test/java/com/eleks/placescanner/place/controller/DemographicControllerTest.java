package com.eleks.placescanner.place.controller;

import static com.eleks.placescanner.place.Util.JSON_OBJECTS_FOLDER;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.placescanner.common.exception.ControllerExceptionHandler;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.place.service.DemographicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DemographicControllerTest {

    private static final String DEMOGRAPHIC_URL = "/demographic";
    private static final String DEMOGRAPHICS_ADVANCE_RESPONSE_PATH =
            JSON_OBJECTS_FOLDER + "demographicsSegmentationAdvancedResponse.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DemographicController controller;

    @MockBean
    private DemographicService demographicService;

    private ObjectMapper objectMapper;
    private PlaceRequest placeRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();


        placeRequest = new PlaceRequest("Wheeling", "Illinois", "60090");

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void shouldReturnDemographicResponse() throws Exception {
        DemographicResponse expectedDemographicResponse = objectMapper.readValue(
                new File(DEMOGRAPHICS_ADVANCE_RESPONSE_PATH),
                DemographicResponse.class);

        when(demographicService.getPopulationForPlace(any(), any())).thenReturn(expectedDemographicResponse);

        mockMvc.perform(post(DEMOGRAPHIC_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDemographicResponse)));
    }

    @Test
    void shouldReturnResourceNotFoundException_whenPreciselyReturnsEmpty() throws Exception {
        when(demographicService.getPopulationForPlace(any(), any())).thenThrow(new ResourceNotFoundException("test"));

        mockMvc.perform(post(DEMOGRAPHIC_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void shouldReturnResourceNotFoundException_whenPromaptoolsReturnsException() throws Exception {
        when(demographicService.getPopulationForPlace(any(), any())).thenThrow(new UnexpectedResponseException("test"));

        mockMvc.perform(post(DEMOGRAPHIC_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnexpectedResponseException))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("test"))
                .andExpect(jsonPath("$.description").value("uri=" + DEMOGRAPHIC_URL));
    }
}