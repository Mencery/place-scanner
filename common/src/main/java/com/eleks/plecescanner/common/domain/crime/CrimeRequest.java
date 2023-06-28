package com.eleks.plecescanner.common.domain.crime;

public record CrimeRequest(
        double longitude,
        double latitude,
        String type,
        String includeGeometry) {
}
