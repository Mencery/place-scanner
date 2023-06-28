package com.eleks.plecescanner.common.domain.pollution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record Pollution(@JsonProperty(value = "aqius") int aqius) {
}