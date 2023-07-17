package com.eleks.plecescanner.common.domain;

import com.eleks.plecescanner.common.domain.pollution.AirResponse;
import com.eleks.plecescanner.common.domain.population.PopClockResponse;
import com.eleks.plecescanner.common.exception.domain.ErrorMessage;
import com.eleks.plecescanner.dao.domain.StateTaxDto;

import java.util.List;

public record PlaceResponse(
        Demographic demographic,
        Crime crime,
        StateTaxDto stateTax,
        PopClockResponse popClockResponse,
        AirResponse airInfo,
        List<ErrorMessage> errorMessages
) {
}
