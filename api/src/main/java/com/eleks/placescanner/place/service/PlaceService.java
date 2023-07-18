package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.PlaceResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<ErrorMessage> errorMessages = new ArrayList<>();
        return new PlaceResponse(
                demographicService.getDemographicInfo(request, securityToken, errorMessages),
                crimeService.getPlaceCrime(request, securityToken, errorMessages),
                stateTaxService.getStateTax(request.state()),
                usPopulationService.findUsPopulation(),
                airConditionService.getAirInfo(request, securityToken, errorMessages),
                errorMessages
        );
    }
}
