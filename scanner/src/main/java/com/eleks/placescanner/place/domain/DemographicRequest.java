package com.eleks.placescanner.place.domain;

public record DemographicRequest(
        double longitude,
        double latitude,
        String profile,
        String filter,
        String valueFormat,
        String variableLevel
) {
}
