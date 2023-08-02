package com.eleks.placescanner.place.service.census;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eleks.placescanner.common.domain.population.PopClockResponse;
import java.util.Date;
import org.junit.jupiter.api.Test;

class CensusServiceTest {

    private final CensusClient censusClient = mock(CensusClient.class);
    private final CensusService service = new CensusService(censusClient);

    @Test
    void shouldReturnUsPopulation() {
        PopClockResponse expectedResponse = new PopClockResponse(100000, new Date(0L));

        when(censusClient.callPopulationByClock()).thenReturn(expectedResponse);

        PopClockResponse actualResponse = service.getPopulationByClock();

        verify(censusClient, times(1)).callPopulationByClock();
        assertEquals(expectedResponse, actualResponse);
    }
}