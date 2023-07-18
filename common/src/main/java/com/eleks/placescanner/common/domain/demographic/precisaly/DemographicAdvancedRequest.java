package com.eleks.placescanner.common.domain.demographic.precisaly;

import com.eleks.placescanner.common.domain.demographic.precisaly.polygon.Geometry;
import com.eleks.placescanner.common.domain.demographic.precisaly.polygon.Preferences;

public record DemographicAdvancedRequest(Preferences preferences, Geometry geometry) {
    public DemographicAdvancedRequest(Geometry geometry) {
        this(new Preferences(), geometry);
    }
}
