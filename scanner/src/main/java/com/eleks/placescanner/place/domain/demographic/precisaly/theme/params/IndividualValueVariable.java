package com.eleks.placescanner.place.domain.demographic.precisaly.theme.params;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IndividualValueVariable(String name, String description, String year, String value) {
}
