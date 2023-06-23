package com.eleks.plecescanner.common.domain.crime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CrimeIndexTheme(
        String source,
        String boundaryRef,
        List<IndexVariable> indexVariable) {
}
