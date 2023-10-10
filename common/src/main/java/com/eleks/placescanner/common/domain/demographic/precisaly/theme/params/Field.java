package com.eleks.placescanner.common.domain.demographic.precisaly.theme.params;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Field(String value, String name, String description) {
}