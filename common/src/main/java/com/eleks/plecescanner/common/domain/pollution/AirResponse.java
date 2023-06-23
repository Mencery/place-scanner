package com.eleks.plecescanner.common.domain.pollution;

import com.eleks.plecescanner.common.domain.pollution.weather.WeatherResponse;

public record AirResponse(PollutionResponse pollution, WeatherResponse weather) {
}
