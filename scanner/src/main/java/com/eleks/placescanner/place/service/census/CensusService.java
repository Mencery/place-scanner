package com.eleks.placescanner.place.service.census;

import com.eleks.plecescanner.common.domain.population.PopClockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CensusService {

    @Autowired
    private CensusClient censusClient;

    public PopClockResponse getPopulationByClock() {
        return censusClient.callPopulationByClock();
    }
}
