package com.eleks.placescanner.common.domain.demographic.precisaly;

public record DemographicRequest(
        double longitude,
        double latitude,
        String profile,
        String filter,
        String valueFormat,
        String variableLevel) {
}
