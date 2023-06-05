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
    private ScannerClient scannerClient;

    @Autowired
    StateTaxService stateTaxService;

    @Autowired
    UsPopulationService usPopulationService;

    @Autowired
    CrimeService crimeService;

    public PlaceResponse getPlaceInfo(PlaceRequest request) {

        return new PlaceResponse(
                getDemographicInfo(request),//todo create demographic service
                crimeService.getPlaceCrime(request),
                stateTaxService.getStateTax(request.state()),
                usPopulationService.findUsPopulation(),
                scannerClient.getPollution(request)
        );
    }



    public Demographic getDemographicInfo(PlaceRequest request) {
        var demographicInfo = scannerClient.callDemographicAdvance(request);

        var populationTheme = demographicInfo.themes().populationTheme();
        var totalPopulation = findIndividualValueVariable(populationTheme, POPCY);

        var raceAndEthnicityTheme = demographicInfo.themes().raceAndEthnicityTheme();
        var raceInfo = findRangeVariable(raceAndEthnicityTheme, RACEPCX);
        var race = new Race(raceInfo.field());

        var incomeTheme = demographicInfo.themes().incomeTheme();
        var householdAverageIncome = findIndividualValueVariable(incomeTheme, HIAVGCY);
        var income = new Income(new BigDecimal(householdAverageIncome.value()));

        var housingTheme = demographicInfo.themes().housingTheme();
        var averageHomeValue = findIndividualValueVariable(housingTheme, HVAVGCY);
        var medianHomeValue = findIndividualValueVariable(housingTheme, HVMEDCY);
        var averageRentValue = findIndividualValueVariable(housingTheme, CRAVGCY);
        var medianRentValue = findIndividualValueVariable(housingTheme, CRMEDCY);
        var housing = new Housing(
                new BigDecimal(averageHomeValue.value()),
                new BigDecimal(medianHomeValue.value()),
                new BigDecimal(averageRentValue.value()),
                new BigDecimal(medianRentValue.value())
        );

        var vehicleInfo = findRangeVariable(housingTheme, VEHICLEHHCX);
        var vehicle = new Vehicle(vehicleInfo.field());

        return new Demographic(totalPopulation.value(), race, income, housing, vehicle);
    }

    private IndividualValueVariable findIndividualValueVariable(Theme theme, String keyword) {
        var optional = theme.individualValueVariable()
                .stream()
                .filter(it -> Objects.equals(it.name(), keyword))
                .findFirst();
        checkOptional(optional);
        return optional.get();
    }

    private RangeVariable findRangeVariable(Theme theme, String keyword) {
        var optional = theme.rangeVariable()
                .stream()
                .filter(it -> Objects.equals(it.name(), keyword))
                .findFirst();
        checkOptional(optional);
        return optional.get();
    }

    private <T> void checkOptional(Optional<T> optional) {
        if (optional.isEmpty()) {
            throw new IllegalStateException("incorrect demographicInfo response");
        }
    }
}
