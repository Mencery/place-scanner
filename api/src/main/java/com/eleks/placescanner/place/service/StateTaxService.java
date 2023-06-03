package com.eleks.placescanner.place.service;

import com.eleks.plecescanner.dao.domain.StateTaxDto;
import com.eleks.plecescanner.dao.repository.StateTaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateTaxService {

    @Autowired
    StateTaxRepository stateTaxRepository;

    public StateTaxDto getStateTax(String state) {
        var stateTaxEntity = stateTaxRepository.findStateTaxByState(state).orElseThrow(() -> new IllegalStateException("State is incorrect"));
        return new StateTaxDto(stateTaxEntity);
    }
}
