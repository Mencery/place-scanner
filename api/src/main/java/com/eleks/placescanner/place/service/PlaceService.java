package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.PlaceResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    private final DemographicService demographicService;
    private final CrimeApiService crimeApiService;
    private final AirConditionService airConditionService;
    private final StateTaxService stateTaxService;
    private final UsPopulationService usPopulationService;

    @Autowired
    public PlaceService(DemographicService demographicService,
                        CrimeApiService crimeApiService,
                        AirConditionService airConditionService,
                        StateTaxService stateTaxService,
                        UsPopulationService usPopulationService) {
        this.demographicService = demographicService;
        this.crimeApiService = crimeApiService;
        this.airConditionService = airConditionService;
        this.stateTaxService = stateTaxService;
        this.usPopulationService = usPopulationService;
    }


    public PlaceResponse getPlaceInfo(PlaceRequest request, String securityToken) {
        List<ErrorMessage> errorMessages = new CopyOnWriteArrayList<>();

        var demographic = demographicService.getDemographicInfo(request, securityToken, errorMessages);
        var crime = crimeApiService.getPlaceCrime(request, securityToken, errorMessages);
        var stateTax = stateTaxService.getStateTax(request.state());
        var usPopulation = usPopulationService.findUsPopulation();
        var airCondition = airConditionService.getAirInfo(request, securityToken, errorMessages);
        try {
            return new PlaceResponse(
                    demographic.get(),
                    crime.get(),
                    stateTax.get(),
                    usPopulation.get(),
                    airCondition.get(),
                    errorMessages
            );
        } catch (InterruptedException | ExecutionException e) {
            throw new UnexpectedResponseException(e.getMessage());
        }
    }
}
