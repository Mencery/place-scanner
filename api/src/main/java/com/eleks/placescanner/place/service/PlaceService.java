package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.plecescanner.common.domain.Demographic;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.PlaceResponse;
import com.eleks.plecescanner.common.domain.demographic.Housing;
import com.eleks.plecescanner.common.domain.demographic.Income;
import com.eleks.plecescanner.common.domain.demographic.Race;
import com.eleks.plecescanner.common.domain.demographic.Vehicle;
import com.eleks.plecescanner.common.domain.demographic.precisaly.theme.params.IndividualValueVariable;
import com.eleks.plecescanner.common.domain.demographic.precisaly.theme.params.RangeVariable;
import com.eleks.plecescanner.common.domain.demographic.precisaly.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static com.eleks.placescanner.place.service.scanner.ThemeKeywords.*;

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

    public PlaceResponse getPlaceInfo(PlaceRequest request) {

        return new PlaceResponse(
                demographicService.getDemographicInfo(request),//todo create demographic service
                crimeService.getPlaceCrime(request),
                stateTaxService.getStateTax(request.state()),
                usPopulationService.findUsPopulation(),
                airConditionService.getAirInfo(request)
        );
    }
}
