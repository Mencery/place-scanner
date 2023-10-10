package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.PlaceResponse;
import com.eleks.placescanner.common.exception.ControllerExceptionHandler;
import com.eleks.placescanner.common.exception.domain.InvalidRequestException;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.place.service.PlaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PlaceControllerTest {
    private static final String GET_PLACE_URL = "/place-info";
    private static final String POST_PLACE_URL = "/places/place-info";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceController controller;

    @MockBean
    private PlaceService placeService;

    private ObjectMapper objectMapper;
    private PlaceRequest placeRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        placeRequest = new PlaceRequest("test", "test", "11111");

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
    void shouldReturnPlaceResponse() throws Exception {
        var expectedPlaceResponse = new PlaceResponse(null, null, null, null, null, null);

        when(placeService.getPlaceInfo(any(), any())).thenReturn(expectedPlaceResponse);

        mockMvc.perform(get(GET_PLACE_URL + "?placeName=test&state=test&zipCode=11111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPlaceResponse)));
    }

    @Test
    void shouldReturnResourceNotFoundException_whenSomethingNotFoundReturnsEmpty() throws Exception {
        when(placeService.getPlaceInfo(any(), any())).thenThrow(new ResourceNotFoundException("test"));

        mockMvc.perform(get(GET_PLACE_URL + "?placeName=Wheeling&state=test&zipCode=11111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }


    @Test
    void shouldReturnUnexpectedResponseException_whenSomethingReturnsException() throws Exception {
        when(placeService.getPlaceInfo(any(), any())).thenThrow(new UnexpectedResponseException("test"));

        mockMvc.perform(get(GET_PLACE_URL + "?placeName=test&state=test&zipCode=11111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnexpectedResponseException))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("test"))
                .andExpect(jsonPath("$.description").value("uri=" + GET_PLACE_URL));
    }

    @Test
    void shouldReturnInvalidRequestExceptionException_whenPlaceMissingReturnsException() throws Exception {
        mockMvc.perform(get(GET_PLACE_URL + "?placeName=&state=test&zipCode=11111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestException))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("placeName cannot be empty"))
                .andExpect(jsonPath("$.description").value("uri=" + GET_PLACE_URL));
    }

    @Test
    void shouldReturnInvalidRequestExceptionException_whenStateMissingReturnsException() throws Exception {
        mockMvc.perform(get(GET_PLACE_URL + "?placeName=test&state=&zipCode=11111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestException))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("state cannot be empty"))
                .andExpect(jsonPath("$.description").value("uri=" + GET_PLACE_URL));
    }

    @Test
    void shouldReturnInvalidRequestExceptionException_whenZipMissingReturnsException() throws Exception {
        mockMvc.perform(get(GET_PLACE_URL + "?placeName=test&state=test&zipCode=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestException))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("zipCode cannot be empty"))
                .andExpect(jsonPath("$.description").value("uri=" + GET_PLACE_URL));
    }

    @Test
    void shouldReturnInvalidRequestExceptionException_whenZipIsnt5DigitsReturnsException() throws Exception {
        mockMvc.perform(get(GET_PLACE_URL + "?placeName=test&state=test&zipCode=111r")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestException))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("zipCode should be 5 digits"))
                .andExpect(jsonPath("$.description").value("uri=" + GET_PLACE_URL));
    }

    @Test
    void shouldReturnPlaceResponsePost() throws Exception {
        var expectedPlaceResponse = new PlaceResponse(null, null, null, null, null, null);

        when(placeService.getPlaceInfo(any(), any())).thenReturn(expectedPlaceResponse);

        mockMvc.perform(post(POST_PLACE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPlaceResponse)));
    }

    @Test
    void shouldReturnResourceNotFoundExceptionPost_whenSomethingNotFoundReturnsEmpty() throws Exception {
        when(placeService.getPlaceInfo(any(), any())).thenThrow(new ResourceNotFoundException("test"));

        mockMvc.perform(post(POST_PLACE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }


    @Test
    void shouldReturnUnexpectedResponseExceptionPost_whenSomethingReturnsException() throws Exception {
        when(placeService.getPlaceInfo(any(), any())).thenThrow(new UnexpectedResponseException("test"));

        mockMvc.perform(post(POST_PLACE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))
                        .characterEncoding("utf-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnexpectedResponseException))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("test"))
                .andExpect(jsonPath("$.description").value("uri=" + POST_PLACE_URL));
    }
}