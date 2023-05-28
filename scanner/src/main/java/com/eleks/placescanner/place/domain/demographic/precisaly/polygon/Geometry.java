package com.eleks.placescanner.place.domain.demographic.precisaly.polygon;

import java.util.List;

public record Geometry(
         String type,
         Crs crs,
         List<List<Double>> coordinates
         ) {
    public Geometry(List<List<Double>> coordinates){
        this("Polygon", new Crs(), coordinates);
    }
}
