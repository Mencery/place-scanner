package com.eleks.plecescanner.common.domain.demographic.precisaly.theme;

import com.eleks.plecescanner.common.domain.demographic.precisaly.theme.params.IndividualValueVariable;
import com.eleks.plecescanner.common.domain.demographic.precisaly.theme.params.RangeVariable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
@JsonIgnoreProperties(ignoreUnknown = true)
public record RaceAndEthnicityTheme(
        ArrayList<IndividualValueVariable> individualValueVariable,
        ArrayList<RangeVariable> rangeVariable
) {
}
