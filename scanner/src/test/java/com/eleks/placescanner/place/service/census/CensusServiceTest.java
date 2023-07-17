package com.eleks.placescanner.place.service.census;

import com.eleks.plecescanner.common.domain.population.PopClockResponse;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CensusServiceTest {

    private final CensusClient censusClient = mock(CensusClient.class);
    private final CensusService service = new CensusService(censusClient);

    @Test
    void shouldReturnUSPopulation() {
        PopClockResponse expectedResponse = new PopClockResponse(100000, new Date(0L));

        when(censusClient.callPopulationByClock()).thenReturn(expectedResponse);

        PopClockResponse actualResponse = service.getPopulationByClock();

        verify(censusClient, times(1)).callPopulationByClock();
        assertEquals(expectedResponse, actualResponse);
    }
}