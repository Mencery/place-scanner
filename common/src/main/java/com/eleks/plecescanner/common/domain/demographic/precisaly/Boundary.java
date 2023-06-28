package com.eleks.plecescanner.common.domain.demographic.precisaly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Boundary(String boundaryId, String boundaryType, String boundaryRef) {
}