package com.eleks.plecescanner.common.domain.crime;

import com.eleks.plecescanner.common.domain.demographic.precisaly.Boundaries;
import com.eleks.plecescanner.common.domain.demographic.precisaly.Themes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CrimeResponse(
        @JsonProperty("themes") List<Theme> themes
) {
}
