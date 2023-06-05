package com.eleks.plecescanner.common.domain.pollution;

public enum AirQuality {
    GOOD("Good", 50),
    MODERATE("Moderate", 100),
    UNHEALTHY_FOR_SENSITIVE_GROUP("Unhealthy for sensitive group", 150),
    UNHEALTHY("Good", 200),
    VERY_UNHEALTHY("Good", 300),
    HAZARDOUS("Hazardous", 301);

    public final String airQualityName;
    public final int airQualityIndex;

    AirQuality(String airQualityName, int airQualityIndex) {
        this.airQualityName = airQualityName;
        this.airQualityIndex = airQualityIndex;
    }
}
