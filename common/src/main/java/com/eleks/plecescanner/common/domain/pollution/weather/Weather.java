package com.eleks.plecescanner.common.domain.pollution.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Weather(@JsonProperty(value = "tp") int temperature) {
}
