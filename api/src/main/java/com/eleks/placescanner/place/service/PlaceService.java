package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.PlaceResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
        List<ErrorMessage> errorMessages = new CopyOnWriteArrayList<>();
        try {
            return new PlaceResponse(
                    demographicService.getDemographicInfo(request, securityToken, errorMessages).get(),
                    crimeService.getPlaceCrime(request, securityToken, errorMessages).get(),
                    stateTaxService.getStateTax(request.state()).get(),
                    usPopulationService.findUsPopulation().get(),
                    airConditionService.getAirInfo(request, securityToken, errorMessages).get(),
                    errorMessages
            );
        } catch (InterruptedException | ExecutionException e) {
            throw new UnexpectedResponseException(e.getMessage());
        }
    }
}
