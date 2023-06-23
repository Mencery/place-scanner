package com.eleks.plecescanner.common.domain.pollution.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Weather(@JsonProperty(value = "tp") int temperature) {
}
