package com.eleks.plecescanner.common.domain.demographic;

import java.math.BigDecimal;

public record Housing(
        BigDecimal averageHomeValue,
        BigDecimal medianHomeValue,
        BigDecimal averageContractRent,
        BigDecimal medianContractRent
) {
}
