package com.eleks.plecescanner.common.domain.demographic.precisaly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DemographicResponse(Boundaries boundaries,
                                  Themes themes) {

}


