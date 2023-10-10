package com.eleks.placescanner.common.domain.crime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IndexVariable(
         String name,
         String score,
         String category,
         String percentile,
         String stateScore) {
}
