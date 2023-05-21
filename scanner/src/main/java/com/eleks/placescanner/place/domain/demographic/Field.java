package com.eleks.placescanner.place.domain.demographic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Field(String value, String name, String description) {
}