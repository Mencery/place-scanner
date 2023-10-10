package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.dao.domain.StateTaxDto;
import com.eleks.placescanner.dao.repository.StateTaxRepository;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateTaxService {

    private final StateTaxRepository stateTaxRepository;

    @Autowired
    public StateTaxService(StateTaxRepository stateTaxRepository) {
        this.stateTaxRepository = stateTaxRepository;
    }

    public CompletableFuture<StateTaxDto> getStateTax(String state) {
        return CompletableFuture.supplyAsync(() -> {
            var stateTaxEntity = stateTaxRepository.findStateTaxByState(state)
                    .orElseThrow(() -> new UnexpectedResponseException("State is incorrect"));
            return new StateTaxDto(stateTaxEntity);
        });
    }
}
