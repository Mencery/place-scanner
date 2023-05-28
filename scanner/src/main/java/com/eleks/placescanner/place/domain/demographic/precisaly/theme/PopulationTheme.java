package com.eleks.placescanner.place.domain.demographic.precisaly.theme;

import com.eleks.placescanner.place.domain.demographic.precisaly.theme.params.IndividualValueVariable;
import com.eleks.placescanner.place.domain.demographic.precisaly.theme.params.RangeVariable;

import java.util.ArrayList;

public record PopulationTheme(
        ArrayList<IndividualValueVariable> individualValueVariable,

        ArrayList<RangeVariable> rangeVariable
) {
}
