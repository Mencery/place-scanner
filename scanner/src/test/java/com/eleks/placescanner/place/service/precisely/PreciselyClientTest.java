package com.eleks.placescanner.place.service.precisely;

import com.eleks.placescanner.common.domain.crime.CrimeRequest;
import com.eleks.placescanner.common.domain.crime.CrimeResponse;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicAdvancedRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.placescanner.common.domain.demographic.precisaly.PreciselyToken;
import com.eleks.placescanner.common.domain.demographic.precisaly.polygon.Geometry;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PreciselyClientTest {

    private static final String DEMOGRAPHICS_BY_LOCATION_RESPONSE_PATH = "./src/test/resources/json_objects/demographicsByLocationResponse.json";
    private static final String DEMOGRAPHICS_ADVANCE_RESPONSE_PATH = "./src/test/resources/json_objects/demographicsSegmentationAdvancedResponse.json";
    private static final String DEMOGRAPHICS_ADVANCE_COORDINATES_PATH = "./src/test/resources/json_objects/demographicAdvanceCoordinates.json";
    private static final String CRIME_BY_LOCATION_RESPONSE_PATH = "./src/test/resources/json_objects/crimeByLocationResponse.json";

    @SpyBean
    private PreciselyClient preciselyClient;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${url.api.precisely.oauth-token}")
    private String OAUTH_TOKEN_URI;

    @Value("#{systemEnvironment['PRECISELY_API_KEY']}")
    private String PRECISELY_API_KEY;

    @Value("#{systemEnvironment['PRECISELY_API_SECRET']}")
    private String PRECISELY_API_SECRET;

    @Value("${url.api.precisely.demographic-by-location}")
    private String demographicByLocationURI;

    private ObjectMapper objectMapper;
    private ResponseEntity response;
    private DemographicRequest demographicRequest;
    private DemographicAdvancedRequest demographicAdvancedRequest;
    private CrimeRequest crimeRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        demographicRequest = new DemographicRequest(
                -87.918620,
                42.135763,
                "Top3Descending",
                "expendituretheme",
                "perCentasAVailable",
                "detailed"
        );
        demographicAdvancedRequest = new DemographicAdvancedRequest(new Geometry(getCoordinates()));
        crimeRequest = new CrimeRequest(-87.918620, 42.135763, "all", "N");
        response = mock(ResponseEntity.class);
        when(response.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
    }

    @Test
    void shouldReturnDemographicByLocation() throws IOException {
        DemographicResponse expectedResponse = objectMapper.readValue(
                new File(DEMOGRAPHICS_ADVANCE_RESPONSE_PATH),
                DemographicResponse.class);

        doReturn("token").when(preciselyClient).getSecurityToken();
        when(response.getBody()).thenReturn(expectedResponse);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);

        DemographicResponse actualResponse = preciselyClient.callDemographicByLocation(demographicRequest);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldReturnDemographicByLocation_whenReturnNotFound() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(UnexpectedResponseException.class, () -> preciselyClient.callDemographicByLocation(demographicRequest), HttpStatusCode.valueOf(404).toString());
    }

    @Test
    void shouldReturnDemographicByLocation_whenReturnServerError() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(UnexpectedResponseException.class, () -> preciselyClient.callDemographicByLocation(demographicRequest), HttpStatusCode.valueOf(500).toString());
    }

    @Test
    void shouldReturnDemographicByLocation_whenReturnNull() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> preciselyClient.callDemographicByLocation(demographicRequest),
                "callDemographicByLocation exception returns empty body");
    }

    @Test
    void shouldReturnDemographicAdvance() throws IOException {
        DemographicResponse expectedResponse = objectMapper.readValue(
                new File(DEMOGRAPHICS_BY_LOCATION_RESPONSE_PATH),
                DemographicResponse.class);

        doReturn("token").when(preciselyClient).getSecurityToken();
        when(response.getBody()).thenReturn(expectedResponse);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);

        DemographicResponse actualResponse = preciselyClient.callDemographicAdvance(demographicAdvancedRequest);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldReturnDemographicAdvance_whenReturnBadRequest() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(400)));

        Assertions.assertThrows(UnexpectedResponseException.class, () -> preciselyClient.callDemographicAdvance(demographicAdvancedRequest), HttpStatusCode.valueOf(400).toString());
    }

    @Test
    void shouldReturnDemographicAdvance_whenReturnServerError() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(500)));

        Assertions.assertThrows(UnexpectedResponseException.class, () -> preciselyClient.callDemographicAdvance(demographicAdvancedRequest), HttpStatusCode.valueOf(500).toString());
    }

    @Test
    void shouldReturnDemographicAdvance_whenResponseNull() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> preciselyClient.callDemographicAdvance(demographicAdvancedRequest), "callDemographicAdvance exception returns empty body");
    }

    @Test
    void shouldReturnCrimeByLocation() throws IOException {
        CrimeResponse expectedResponse = objectMapper.readValue(
                new File(CRIME_BY_LOCATION_RESPONSE_PATH),
                CrimeResponse.class);

        doReturn("token").when(preciselyClient).getSecurityToken();
        when(response.getBody()).thenReturn(expectedResponse);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);

        CrimeResponse actualResponse = preciselyClient.callCrimeByLocation(crimeRequest);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldReturnCrimeByLocation_whenReturnBadRequest() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(400)));

        Assertions.assertThrows(UnexpectedResponseException.class, () -> preciselyClient.callCrimeByLocation(crimeRequest), HttpStatusCode.valueOf(400).toString());
    }

    @Test
    void shouldReturnCrimeByLocation_whenReturnServerError() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(500)));

        Assertions.assertThrows(UnexpectedResponseException.class, () -> preciselyClient.callCrimeByLocation(crimeRequest), HttpStatusCode.valueOf(500).toString());
    }

    @Test
    void shouldReturnCrimeByLocation_whenResponseNull() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> preciselyClient.callCrimeByLocation(crimeRequest), "callCrimeByLocation exception returns empty body");
    }

    @Test
    void shouldReturnSecurityToken() {

        var preciselyToken = new PreciselyToken("token", "", "", "", "", "");

        when(response.getBody()).thenReturn(preciselyToken);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);
        var accessToken = preciselyClient.getSecurityToken();
        Assertions.assertEquals("Bearer token", accessToken);
    }

    @Test
    void shouldReturnSecurityTokenThrowsError_whenResponseBodyNull() {
        when(response.getBody()).thenReturn(null);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> preciselyClient.getSecurityToken(), "no token");
    }

    @Test
    void shouldReturnSecurityTokenThrowsError_whenAccessTokenNull() {
        var preciselyToken = new PreciselyToken(null, "", "", "", "", "");

        when(response.getBody()).thenReturn(preciselyToken);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> preciselyClient.getSecurityToken(), "no token");
    }

    @Test
    void shouldReturnSecurityTokenThrowsError_whenAccessTokenEmpty() {
        var preciselyToken = new PreciselyToken("", "", "", "", "", "");

        when(response.getBody()).thenReturn(preciselyToken);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> preciselyClient.getSecurityToken(), "no token");
    }

    private URI getDemographicByLocationURI(DemographicRequest request) {
        return UriComponentsBuilder.fromUriString(demographicByLocationURI + "?"
                + "longitude=" + request.longitude() + "&"
                + "latitude=" + request.latitude() + "&"
                + "profile=" + request.profile() + "&"
                + "filter=" + request.filter() + "&"
                + "valueFormat=" + request.valueFormat() + "&"
                + "variableLevel=" + request.variableLevel() + "&"
        ).build().toUri();
    }

    @SuppressWarnings("unchecked")
    private List<List<Double>> getCoordinates() {
        try {
            var list = objectMapper.readValue(
                    new File(DEMOGRAPHICS_ADVANCE_COORDINATES_PATH),
                    List.class);

            return (List<List<Double>>) list;

        } catch (ClassCastException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}