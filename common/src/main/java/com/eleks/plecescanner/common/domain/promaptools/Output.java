package com.eleks.plecescanner.common.domain.promaptools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Output(
        String zip,
        String latitude,
        String longitude
) {
}
