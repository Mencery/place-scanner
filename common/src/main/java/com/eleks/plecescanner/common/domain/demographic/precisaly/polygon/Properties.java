package com.eleks.plecescanner.common.domain.demographic.precisaly.polygon;

public record Properties(String name) {
    public Properties(){
        this("epsg:4326");
    }
}
