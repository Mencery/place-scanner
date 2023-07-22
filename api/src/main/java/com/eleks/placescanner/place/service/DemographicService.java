package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.placescanner.common.domain.Demographic;
import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.demographic.Housing;
import com.eleks.placescanner.common.domain.demographic.Income;
import com.eleks.placescanner.common.domain.demographic.Race;
import com.eleks.placescanner.common.domain.demographic.Vehicle;
import com.eleks.placescanner.common.domain.demographic.precisaly.theme.Theme;
import com.eleks.placescanner.common.domain.demographic.precisaly.theme.params.IndividualValueVariable;
import com.eleks.placescanner.common.domain.demographic.precisaly.theme.params.RangeVariable;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.eleks.placescanner.place.service.scanner.ThemeKeywords.*;
import static com.eleks.placescanner.place.service.scanner.ThemeKeywords.VEHICLEHHCX;

@Service
public class DemographicService {

    private ScannerClient scannerClient;

    @Autowired
    public DemographicService(ScannerClient scannerClient) {
        this.scannerClient = scannerClient;
    }

    public CompletableFuture<Demographic> getDemographicInfo(PlaceRequest request, String securityToken, List<ErrorMessage> errorMessages) {
        return CompletableFuture.supplyAsync(() -> {
            var demographicInfo = scannerClient.callDemographicAdvance(request, securityToken, errorMessages);

            if (demographicInfo == null) {
                return null;
            }

            //population
            var populationTheme = demographicInfo.themes().populationTheme();
            var totalPopulation = findIndividualValueVariable(populationTheme, POPCY);

            //race
            var raceAndEthnicityTheme = demographicInfo.themes().raceAndEthnicityTheme();
            var raceInfo = findRangeVariable(raceAndEthnicityTheme, RACEPCX);
            var race = new Race(raceInfo.field());

            //income
            var incomeTheme = demographicInfo.themes().incomeTheme();
            var householdAverageIncome = findIndividualValueVariable(incomeTheme, HIAVGCY);
            var income = new Income(new BigDecimal(householdAverageIncome.value()));

            //housing
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

            //vehicle
            var vehicleInfo = findRangeVariable(housingTheme, VEHICLEHHCX);
            var vehicle = new Vehicle(vehicleInfo.field());

            return new Demographic(totalPopulation.value(), race, income, housing, vehicle);
        });
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
