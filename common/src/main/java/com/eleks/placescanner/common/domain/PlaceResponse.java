package com.eleks.placescanner.common.domain;

import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.common.domain.population.PopClockResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.eleks.placescanner.dao.domain.StateTaxDto;
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
