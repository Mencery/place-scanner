package com.eleks.plecescanner.common.domain;

import com.eleks.plecescanner.common.domain.pollution.AirResponse;
import com.eleks.plecescanner.common.domain.population.PopClockResponse;
import com.eleks.plecescanner.dao.domain.StateTaxDto;

public record PlaceResponse(
        Demographic demographic,
        Crime crime,
        StateTaxDto stateTax,
        PopClockResponse popClockResponse,
        AirResponse airInfo) {
}
