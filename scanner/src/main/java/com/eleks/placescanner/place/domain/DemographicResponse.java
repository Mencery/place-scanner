package com.eleks.placescanner.place.domain;

import com.eleks.placescanner.place.domain.demographic.Boundaries;
import com.eleks.placescanner.place.domain.demographic.Themes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DemographicResponse(Boundaries boundaries,
                                  Themes themes) {

}


