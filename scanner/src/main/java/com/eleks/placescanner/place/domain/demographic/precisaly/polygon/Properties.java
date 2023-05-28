package com.eleks.placescanner.place.domain.demographic.precisaly.polygon;

public record Properties(String name) {
    public Properties(){
        this("epsg:4326");
    }
}
