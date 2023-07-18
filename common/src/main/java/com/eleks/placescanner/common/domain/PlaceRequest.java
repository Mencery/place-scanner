package com.eleks.placescanner.common.domain;

import com.eleks.placescanner.common.exception.domain.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record PlaceRequest(
        @JsonProperty("placeName") String placeName,
        @JsonProperty("state") String state,
        @JsonProperty("zipCode") String zipCode) {

    public PlaceRequest validateRequest() {
        if (isEmptyString(placeName)) {
            throw new InvalidRequestException("placeName cannot be empty");
        }
        if (isEmptyString(state)) {
            throw new InvalidRequestException("state cannot be empty");
        }
        if (isEmptyString(zipCode)) {
            throw new InvalidRequestException("zipCode cannot be empty");
        }
        if (zipCode.length() != 5 || !zipCode.matches("\\d{5}")) {
            throw new InvalidRequestException("zipCode should be 5 digits");
        }
        return this;
    }

    private boolean isEmptyString(String parameter) {
        return parameter == null || parameter.isEmpty() || parameter.isBlank();
    }
}
