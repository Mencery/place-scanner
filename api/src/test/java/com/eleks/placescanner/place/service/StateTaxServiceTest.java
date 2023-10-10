package com.eleks.placescanner.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.eleks.placescanner.dao.domain.StateTaxDto;
import com.eleks.placescanner.dao.entity.StateTax;
import com.eleks.placescanner.dao.repository.StateTaxRepository;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateTaxServiceTest {

    private StateTaxRepository stateTaxRepository;
    private StateTaxService stateTaxService;

    @BeforeEach
    public void setUp() {
        stateTaxRepository = mock(StateTaxRepository.class);
        stateTaxService = new StateTaxService(stateTaxRepository);
    }

    @Test
    void getStateTax() throws ExecutionException, InterruptedException {
        var stateTax = new StateTax("IL", "test", "test");
        Optional<StateTax> optional = Optional.of(stateTax);

        when(stateTaxRepository.findStateTaxByState("IL")).thenReturn(optional);

        var actual = stateTaxService.getStateTax("IL").get();

        assertEquals(new StateTaxDto(stateTax), actual);
    }

    @Test
    void getStateTax_NotFound() {
        Optional<StateTax> optional = Optional.empty();

        when(stateTaxRepository.findStateTaxByState("IL")).thenReturn(optional);

        assertThrows(CompletionException.class,
                () -> stateTaxService.getStateTax("IL").join(),
                "State is incorrect");
    }
}