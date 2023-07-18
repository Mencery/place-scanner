package com.eleks.placescanner.common.domain.crime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Theme(CrimeIndexTheme crimeIndexTheme) {
}
