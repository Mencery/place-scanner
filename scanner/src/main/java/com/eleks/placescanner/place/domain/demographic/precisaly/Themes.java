package com.eleks.placescanner.place.domain.demographic.precisaly;

import com.eleks.placescanner.place.domain.demographic.precisaly.theme.ExpenditureTheme;
import com.eleks.placescanner.place.domain.demographic.precisaly.theme.PopulationTheme;
import com.eleks.placescanner.place.domain.demographic.precisaly.theme.RaceAndEthnicityTheme;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Themes(PopulationTheme populationTheme, RaceAndEthnicityTheme raceAndEthnicityTheme, ExpenditureTheme expenditureTheme) {
}
