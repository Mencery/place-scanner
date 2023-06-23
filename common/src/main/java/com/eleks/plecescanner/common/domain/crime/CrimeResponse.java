package com.eleks.plecescanner.common.domain.crime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CrimeResponse(@JsonProperty("themes") List<Theme> themes) {
}
