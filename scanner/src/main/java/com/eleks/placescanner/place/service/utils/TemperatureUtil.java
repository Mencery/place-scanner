package com.eleks.placescanner.place.service.utils;

import static org.aspectj.runtime.internal.Conversions.intValue;

public class TemperatureUtil {

    public static int celsiusToFahrenheit(int celsius) {
        return intValue(celsius * 1.8 + 32);
    }
}
