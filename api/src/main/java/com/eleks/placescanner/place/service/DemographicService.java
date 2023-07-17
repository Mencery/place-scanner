package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.plecescanner.common.domain.Demographic;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.demographic.Housing;
import com.eleks.plecescanner.common.domain.demographic.Income;
import com.eleks.plecescanner.common.domain.demographic.Race;
import com.eleks.plecescanner.common.domain.demographic.Vehicle;
import com.eleks.plecescanner.common.domain.demographic.precisaly.theme.Theme;
import com.eleks.plecescanner.common.domain.demographic.precisaly.theme.params.IndividualValueVariable;
import com.eleks.plecescanner.common.domain.demographic.precisaly.theme.params.RangeVariable;
import com.eleks.plecescanner.common.exception.domain.ErrorMessage;
import com.eleks.plecescanner.common.exception.domain.UnexpectedResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.eleks.placescanner.place.service.scanner.ThemeKeywords.*;
import static com.eleks.placescanner.place.service.scanner.ThemeKeywords.VEHICLEHHCX;

@Service
public class DemographicService {

    @Autowired
    private ScannerClient scannerClient;

    public Demographic getDemographicInfo(PlaceRequest request, String securityToken, List<ErrorMessage> errorMessages) {
        var demographicInfo = scannerClient.callDemographicAdvance(request, securityToken, errorMessages);

        if (demographicInfo == null) {
            return null;
        }

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
            throw new UnexpectedResponseException("incorrect demographicInfo response");
        }
    }
}
