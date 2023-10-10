package com.eleks.placescanner.common.domain.pollution;

import com.eleks.placescanner.common.domain.pollution.weather.WeatherResponse;

public record AirResponse(PollutionResponse pollution, WeatherResponse weather) {
}
