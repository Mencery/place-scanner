package com.eleks.plecescanner.common.domain.demographic.nominatim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GetPolygonResponse(GeoJson geojson) {
}
