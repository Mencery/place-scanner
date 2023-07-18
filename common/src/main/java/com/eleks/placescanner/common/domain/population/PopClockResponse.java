package com.eleks.placescanner.common.domain.population;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PopClockResponse(
        @JsonProperty("population")
        int population,
        @JsonProperty("date")
        @JsonFormat(pattern = "MM/dd/yyyy")
        Date date) {
}
