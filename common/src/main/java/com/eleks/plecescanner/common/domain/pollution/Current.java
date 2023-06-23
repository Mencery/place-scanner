package com.eleks.plecescanner.common.domain.pollution;

import com.eleks.plecescanner.common.domain.pollution.weather.Weather;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Current(
        @JsonProperty(value = "pollution") Pollution pollution,
        @JsonProperty(value = "weather") Weather weather) {
}