package com.eleks.placescanner.place.controller.exception;

import com.eleks.placescanner.place.controller.CrimeController;
import com.eleks.placescanner.place.service.CrimeService;
import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.exception.ControllerExceptionHandler;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class ControllerExceptionHandlerTest {

    private static final String CRIME_BY_LOCATION_URL = "/crime-by-location";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CrimeController crimeController;

    @Mock
    private CrimeService crimeService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(crimeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testExceptionThrowingControllerExists() {
        assertNotNull(crimeController);
    }

    @Test
    public void testHandleUnexpectedResponseException() throws Exception {
        PlaceRequest request = new PlaceRequest("test", "test", "test");
        when(crimeService.getPopulationForPlace("test")).thenThrow(new UnexpectedResponseException("test"));

        mockMvc.perform(post(CRIME_BY_LOCATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding("utf-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnexpectedResponseException))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("test"))
                .andExpect(jsonPath("$.description").value("uri=" + CRIME_BY_LOCATION_URL));
    }

    @Test
    public void testHandleResourceNotFoundException() throws Exception {
        PlaceRequest request = new PlaceRequest("test", "test", "test");
        when(crimeService.getPopulationForPlace("test")).thenThrow(new ResourceNotFoundException("testMessage"));

        mockMvc.perform(post(CRIME_BY_LOCATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("testMessage"))
                .andExpect(jsonPath("$.description").value("uri=" + CRIME_BY_LOCATION_URL));
    }
}