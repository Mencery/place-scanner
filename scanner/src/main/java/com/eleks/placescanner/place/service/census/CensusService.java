package com.eleks.placescanner.place.service.census;

import com.eleks.placescanner.common.domain.population.PopClockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CensusService {

    private final CensusClient censusClient;

    @Autowired
    CensusService(CensusClient censusClient){
        this.censusClient = censusClient;
    }

    public PopClockResponse getPopulationByClock() {
        return censusClient.callPopulationByClock();
    }
}
