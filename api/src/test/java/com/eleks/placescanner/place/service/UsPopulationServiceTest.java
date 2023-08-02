package com.eleks.placescanner.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eleks.placescanner.common.domain.population.PopClockResponse;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.dao.entity.UsPopulation;
import com.eleks.placescanner.dao.repository.UsPopulationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsPopulationServiceTest {

    private UsPopulationRepository usPopulationRepository;
    private UsPopulationService usPopulationService;

    @BeforeEach
    public void setUp() {
        usPopulationRepository = mock(UsPopulationRepository.class);
        usPopulationService = new UsPopulationService(usPopulationRepository);
    }

    @Test
    void updateUsPopulation() {
        var popClock = new PopClockResponse(330000000, null);
        var usPopulation = new UsPopulation(popClock.population(), popClock.date());
        var usPopulationList = List.of(usPopulation);

        when(usPopulationRepository.findAll()).thenReturn(usPopulationList);
        when(usPopulationRepository.save(any())).thenReturn(usPopulation);

        usPopulationService.updateUsPopulation(popClock);

        verify(usPopulationRepository, times(1)).save(usPopulation);
    }

    @Test
    void updateUsPopulation_WhenNoDataInDb() {
        var popClock = new PopClockResponse(330000000, null);
        var usPopulation = new UsPopulation(popClock.population(), popClock.date());
        var usPopulationList = new ArrayList<UsPopulation>();

        when(usPopulationRepository.findAll()).thenReturn(usPopulationList);
        when(usPopulationRepository.insert(usPopulation)).thenReturn(usPopulation);

        usPopulationService.updateUsPopulation(popClock);

        verify(usPopulationRepository, times(1)).insert(usPopulation);
    }

    @Test
    void updateUsPopulation_WhenMoreThan2RowsInDb() {
        var popClock = new PopClockResponse(330000000, null);
        var usPopulation = new UsPopulation(popClock.population(), popClock.date());
        var usPopulationList = List.of(usPopulation, usPopulation);

        when(usPopulationRepository.findAll()).thenReturn(usPopulationList);
        when(usPopulationRepository.insert(usPopulation)).thenReturn(usPopulation);

        assertThrows(UnexpectedResponseException.class,
                () -> usPopulationService.updateUsPopulation(popClock),
                "cannot be more or less than one us population");
    }


    @Test
    void getUsPopulation() throws ExecutionException, InterruptedException {
        var usPopulation = new UsPopulation(330000000, null);
        var usPopulationList = List.of(usPopulation);

        when(usPopulationRepository.findAll()).thenReturn(usPopulationList);

        var expected = new PopClockResponse(usPopulation.getPopulation(), usPopulation.getDate());
        var actual = usPopulationService.findUsPopulation().get();

        assertEquals(expected, actual);
    }

    @Test
    void getUsPopulation_NotFound() {
        var usPopulationList = new ArrayList<UsPopulation>();

        when(usPopulationRepository.findAll()).thenReturn(usPopulationList);

        assertThrows(CompletionException.class,
                () -> usPopulationService.findUsPopulation().join(),
                "cannot be more or less than one us population");
    }
}