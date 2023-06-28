package com.eleks.plecescanner.common.domain;

import com.eleks.plecescanner.common.domain.demographic.Housing;
import com.eleks.plecescanner.common.domain.demographic.Income;
import com.eleks.plecescanner.common.domain.demographic.Race;
import com.eleks.plecescanner.common.domain.demographic.Vehicle;

public record Demographic(
        String totalPlacePopulation,
        Race race,
        Income income,
        Housing housing,
        Vehicle vehicle) {
}
