package com.eleks.plecescanner.common.domain.pollution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Data(
        @JsonProperty(value = "current") Current current
) {
}