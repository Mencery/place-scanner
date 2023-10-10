package com.eleks.placescanner.common.domain.demographic;

import java.math.BigDecimal;

public record Housing(
        BigDecimal averageHomeValue,
        BigDecimal medianHomeValue,
        BigDecimal averageContractRent,
        BigDecimal medianContractRent) {
}
