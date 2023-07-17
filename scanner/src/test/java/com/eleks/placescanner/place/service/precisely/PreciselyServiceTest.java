package com.eleks.placescanner.place.service.precisely;

import com.eleks.plecescanner.common.domain.crime.CrimeResponse;
import com.eleks.plecescanner.common.domain.demographic.precisaly.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PreciselyServiceTest {

    private final PreciselyClient preciselyClientMock = mock(PreciselyClient.class);
    private final PreciselyService service = new PreciselyService(preciselyClientMock);

    @Test
    void shouldReturnDemographic() {
        Collection<Boundary> boundariesCollection =
                List.of(new Boundary[]{new Boundary("Test1", "Test1", "Test1")});
        Boundaries boundaries = new Boundaries(new ArrayList<>(boundariesCollection));
        DemographicResponse expected = new DemographicResponse(boundaries, null);

        when(preciselyClientMock.callDemographicByLocation(any())).thenReturn(expected);

        DemographicResponse actual = service.getDemographic(-87.939922, 42.106541);

        verify(preciselyClientMock, times(1)).callDemographicByLocation(any());
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnDemographicByPolygon() {
        Collection<Boundary> boundariesCollection =
                List.of(new Boundary[]{new Boundary("Test1", "Test1", "Test1")});
        Boundaries boundaries = new Boundaries(new ArrayList<>(boundariesCollection));
        DemographicResponse expected = new DemographicResponse(boundaries, null);

        when(preciselyClientMock.callDemographicAdvance(any())).thenReturn(expected);

        DemographicResponse actual = service.getDemographicByPolygon(new ArrayList<>());

        verify(preciselyClientMock, times(1)).callDemographicAdvance(any());
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCrime() {
        CrimeResponse expected = new CrimeResponse(null);

        when(preciselyClientMock.callCrimeByLocation(any())).thenReturn(expected);

        CrimeResponse actual = service.getCrime(0.0, 0.0);

        verify(preciselyClientMock, times(1)).callCrimeByLocation(any());
        assertEquals(expected, actual);
    }
}