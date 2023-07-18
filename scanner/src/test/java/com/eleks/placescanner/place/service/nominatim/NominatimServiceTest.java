package com.eleks.placescanner.place.service.nominatim;

import com.eleks.placescanner.common.domain.demographic.nominatim.GeoJson;
import com.eleks.placescanner.common.domain.demographic.nominatim.GetPolygonResponse;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class NominatimServiceTest {

    private final NominatimClient nominatimClient = mock(NominatimClient.class);
    private final NominatimService service = new NominatimService(nominatimClient);

    @Test
    void shouldReturnPolygon() {
        var expectedResponse = List.of(List.of(41.5, -71.2));
        var geoJson = new GeoJson("Polygon", List.of(expectedResponse));
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        var actualResponse = service.getPolygon("city", "state");

        verify(nominatimClient, times(1)).callPlacePolygon(any());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldReturnPolygon_whenNestedness2() {
        var geoJson = new GeoJson("Polygon", List.of(List.of(41.5, -71.2)));
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        assertThrows(UnexpectedResponseException.class, () -> service.getPolygon("city", "state"));
    }

    @Test
    void shouldReturnPolygon_whenNestedness1() {
        var geoJson = new GeoJson("Polygon", List.of(41.5, -71.2));
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        assertThrows(UnexpectedResponseException.class, () -> service.getPolygon("city", "state"));
    }

    @Test
    void shouldReturnPolygon_whenCoordinatesEmpty() {
        var geoJson = new GeoJson("Polygon", new ArrayList<>());
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        assertThrows(ResourceNotFoundException.class,
                () -> service.getPolygon("city", "state"),
                "coordinates cannot be empty");
    }


    @Test
    void shouldReturnPolygon_whenTypeMultiPolygon() {
        var expectedResponse = List.of(List.of(41.5, -71.2));
        var geoJson = new GeoJson("MultiPolygon", List.of(List.of(expectedResponse)));
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        var actualResponse = service.getPolygon("city", "state");

        verify(nominatimClient, times(1)).callPlacePolygon(any());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldReturnPolygon_whenTypeMultiPolygonAndNestedness3() {
        var geoJson = new GeoJson("MultiPolygon", List.of(List.of(List.of(41.5, -71.2))));
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        assertThrows(UnexpectedResponseException.class,
                () -> service.getPolygon("city", "state"),
                "it's not List<List<Double>> type");
    }

    @Test
    void shouldReturnPolygon_whenTypeMultiPolygonAndNestedness5() {
        var geoJson = new GeoJson("MultiPolygon", List.of(List.of(List.of(List.of(List.of(41.5, -71.2))))));
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        assertThrows(UnexpectedResponseException.class,
                () -> service.getPolygon("city", "state"),
                "it's not List<List<Double>> type");
    }

    @Test
    void shouldReturnPolygon_whenTypeMultiPolygonAndCoordinatesEmpty() {
        var geoJson = new GeoJson("Polygon", new ArrayList<>());
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        assertThrows(ResourceNotFoundException.class,
                () -> service.getPolygon("city", "state"),
                "coordinates cannot be empty");
    }

    @Test
    void shouldReturnPolygon_whenTypeNotPolygonNotMultiPolygon() {
        var geoJson = new GeoJson("N/A", (List.of(List.of(List.of(41.5, -71.2)))));
        GetPolygonResponse response = new GetPolygonResponse(geoJson);

        when(nominatimClient.callPlacePolygon(any())).thenReturn(response);

        assertThrows(UnexpectedResponseException.class,
                () -> service.getPolygon("city", "state"),
                "cannot parse coordinates, unknown type");
    }

}