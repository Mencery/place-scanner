package com.eleks.placescanner.place.service;

import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.PlaceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PlaceService {
    @Autowired
    DemographicService demographicService;

    @Autowired
    StateTaxService stateTaxService;

    @Autowired
    UsPopulationService usPopulationService;

    @Autowired
    CrimeService crimeService;

    @Autowired
    AirConditionService airConditionService;

    public PlaceResponse getPlaceInfo(PlaceRequest request, String securityToken) {

        try {
            return new PlaceResponse(
                    demographicService.getDemographicInfo(request, securityToken).get(),
                    crimeService.getPlaceCrime(request, securityToken).get(),
                    stateTaxService.getStateTax(request.state()).get(),
                    usPopulationService.findUsPopulation().get(),
                    airConditionService.getAirInfo(request, securityToken).get()
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
