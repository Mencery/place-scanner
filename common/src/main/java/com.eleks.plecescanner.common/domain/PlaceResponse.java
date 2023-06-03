package com.eleks.plecescanner.common.domain;

import com.eleks.plecescanner.common.domain.population.PopClockResponse;
import com.eleks.plecescanner.dao.domain.StateTaxDto;

public record PlaceResponse(
        Demographic demographic,
        StateTaxDto stateTax,
        PopClockResponse popClockResponse
) {
}
