package com.eleks.placescanner.common.domain;

import com.eleks.placescanner.common.domain.demographic.Housing;
import com.eleks.placescanner.common.domain.demographic.Income;
import com.eleks.placescanner.common.domain.demographic.Race;
import com.eleks.placescanner.common.domain.demographic.Vehicle;

public record Demographic(
        String totalPlacePopulation,
        Race race,
        Income income,
        Housing housing,
        Vehicle vehicle) {
}
