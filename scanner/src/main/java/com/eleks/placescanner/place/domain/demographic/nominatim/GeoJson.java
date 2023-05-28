package com.eleks.placescanner.place.domain.demographic.nominatim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoJson(
        String type,
        List<Object> coordinates
        //List<List<List<Double>>> coordinates
) {
}
