package com.eleks.placescanner.place.service;

import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.PlaceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return new PlaceResponse(
                demographicService.getDemographicInfo(request, securityToken),
                crimeService.getPlaceCrime(request, securityToken),
                stateTaxService.getStateTax(request.state()),
                usPopulationService.findUsPopulation(),
                airConditionService.getAirInfo(request, securityToken)
        );
    }
}
