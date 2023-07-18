package com.eleks.placescanner.common.domain.demographic.precisaly.polygon;

public record Properties(String name) {
    public Properties() {
        this("epsg:4326");
    }
}
