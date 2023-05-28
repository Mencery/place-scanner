package com.eleks.placescanner.place.domain.demographic.precisaly.polygon;

public record Crs(
        String type,
        Properties properties
) {
    public Crs() {
        this("name", new Properties());
    }
}
