package com.eleks.plecescanner.common.domain.crime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Theme(CrimeIndexTheme crimeIndexTheme) {
}
