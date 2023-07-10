package com.eleks.placescanner.place.service.precisely;

import com.eleks.plecescanner.common.domain.crime.CrimeRequest;
import com.eleks.plecescanner.common.domain.crime.CrimeResponse;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicAdvancedRequest;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicRequest;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.plecescanner.common.domain.demographic.precisaly.PreciselyToken;
import com.eleks.plecescanner.common.domain.demographic.precisaly.polygon.Geometry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PreciselyClientTest {

    @Autowired
    private MockMvc mockMvc;

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

    private static final String DEMOGRAPHICS_BY_LOCATION_RESPONSE_PATH = "./src/test/resources/json_objects/demographicsByLocationResponse.json";
    private static final String DEMOGRAPHICS_ADVANCE_RESPONSE_PATH = "./src/test/resources/json_objects/demographicsSegmentationAdvancedResponse.json";
    private static final String DEMOGRAPHICS_ADVANCE_COORDINATES_PATH = "./src/test/resources/json_objects/demographicAdvanceCoordinates.json";
    private static final String CRIME_BY_LOCATION_RESPONSE_PATH = "./src/test/resources/json_objects/crimeByLocationResponse.json";

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
    void callDemographicByLocation() throws IOException {
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
    void callDemographicByLocationWhenReturnNotFound() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(404)));

        Assertions.assertThrows(HttpServerErrorException.class, () -> preciselyClient.callDemographicByLocation(demographicRequest), HttpStatusCode.valueOf(404).toString());
    }

    @Test
    void callDemographicByLocationWhenReturnServerError() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(500)));

        Assertions.assertThrows(HttpServerErrorException.class, () -> preciselyClient.callDemographicByLocation(demographicRequest), HttpStatusCode.valueOf(500).toString());
    }

    @Test
    void callDemographicByLocationWhenReturnNull() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(null);

        Assertions.assertThrows(IllegalStateException.class,
                () -> preciselyClient.callDemographicByLocation(demographicRequest),
                "callDemographicByLocation exception returns empty body");
    }

    @Test
    void callDemographicAdvance() throws IOException {
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
    void callDemographicAdvanceWhenReturnBadRequest() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(400)));

        Assertions.assertThrows(HttpServerErrorException.class, () -> preciselyClient.callDemographicAdvance(demographicAdvancedRequest), HttpStatusCode.valueOf(400).toString());
    }

    @Test
    void callDemographicAdvanceWhenReturnServerError() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(500)));

        Assertions.assertThrows(HttpServerErrorException.class, () -> preciselyClient.callDemographicAdvance(demographicAdvancedRequest), HttpStatusCode.valueOf(500).toString());
    }

    @Test
    void callDemographicAdvanceWhenResponseNull() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(null);

        Assertions.assertThrows(IllegalStateException.class, () -> preciselyClient.callDemographicAdvance(demographicAdvancedRequest), "callDemographicAdvance exception returns empty body");
    }

    @Test
    void callCrimeByLocation() throws IOException {
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
    void callCrimeByLocationWhenReturnBadRequest() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(400)));

        Assertions.assertThrows(HttpServerErrorException.class, () -> preciselyClient.callCrimeByLocation(crimeRequest), HttpStatusCode.valueOf(400).toString());
    }

    @Test
    void callCrimeByLocationWhenReturnServerError() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(500)));

        Assertions.assertThrows(HttpServerErrorException.class, () -> preciselyClient.callCrimeByLocation(crimeRequest), HttpStatusCode.valueOf(500).toString());
    }

    @Test
    void callCrimeByLocationWhenResponseNull() {
        doReturn("token").when(preciselyClient).getSecurityToken();
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(null);

        Assertions.assertThrows(IllegalStateException.class, () -> preciselyClient.callCrimeByLocation(crimeRequest), "callCrimeByLocation exception returns empty body");
    }

    @Test
    void getSecurityToken() {

        var preciselyToken = new PreciselyToken("token", "", "", "", "", "");

        when(response.getBody()).thenReturn(preciselyToken);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);
        var accessToken = preciselyClient.getSecurityToken();
        Assertions.assertEquals("Bearer token", accessToken);
    }

    @Test
    void securityTokenThrowsErrorWhenResponseBodyNull() {
        when(response.getBody()).thenReturn(null);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);
        Assertions.assertThrows(IllegalStateException.class, () -> preciselyClient.getSecurityToken(), "no token");
    }

    @Test
    void securityTokenThrowsErrorWhenAccessTokenNull() {
        var preciselyToken = new PreciselyToken(null, "", "", "", "", "");

        when(response.getBody()).thenReturn(preciselyToken);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);
        Assertions.assertThrows(IllegalStateException.class, () -> preciselyClient.getSecurityToken(), "no token");
    }

    @Test
    void securityTokenThrowsErrorWhenAccessTokenEmpty() {
        var preciselyToken = new PreciselyToken("", "", "", "", "", "");

        when(response.getBody()).thenReturn(preciselyToken);
        when(restTemplate.exchange(any(), any(ParameterizedTypeReference.class))).thenReturn(response);
        Assertions.assertThrows(IllegalStateException.class, () -> preciselyClient.getSecurityToken(), "no token");
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