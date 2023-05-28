package com.eleks.placescanner.place.domain.demographic.precisaly.polygon;

public record Preferences(
        String includeGeometry,
        Object profile,
        Object filter
) {
   public Preferences(){
        this("N", null, null);
    }
}
