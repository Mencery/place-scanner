package com.eleks.placescanner.common.domain.pollution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record PollutionApiResponse(@JsonProperty(value = "data") Data data) {
}
