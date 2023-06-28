package com.eleks.plecescanner.common.domain.demographic.nominatim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GetPolygonResponse(
        @JsonProperty(value = "place_id") int placeId,
        String licence,
        @JsonProperty(value = "osm_type") String osmType,
        @JsonProperty(value = "osm_id") long osmId,
        List<String> boundingbox,
        String lat,
        String lon,
        @JsonProperty(value = "display_name") String displayName,
        @JsonProperty(value = "place_rank") int placeRank,
        String category,
        String type,
        double importance,
        String icon,
        GeoJson geojson) {
}
