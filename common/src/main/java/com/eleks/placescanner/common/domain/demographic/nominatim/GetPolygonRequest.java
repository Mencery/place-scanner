package com.eleks.placescanner.common.domain.demographic.nominatim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GetPolygonRequest(String placeName, String state) {
}
