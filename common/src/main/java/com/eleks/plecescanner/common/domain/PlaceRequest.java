package com.eleks.plecescanner.common.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record PlaceRequest(
        @JsonProperty("placeName") String placeName,
        @JsonProperty("state") String state,
        @JsonProperty("zipCode") String zipCode
) {

    public PlaceRequest validateRequest(){
        if(isEmptyString(placeName)){
            throw new IllegalStateException("placeName cannot be empty");
        }
        if (isEmptyString(state)){
            throw new IllegalStateException("state cannot be empty");
        }
        if (isEmptyString(zipCode)){
            throw new IllegalStateException("zipCode cannot be empty");
        }
        if (zipCode.length() != 5 || !zipCode.matches("\\d{5}")){
            throw new IllegalStateException("zipCode should be 5 digits");
        }
        return this;
    }

    private boolean isEmptyString(String s){
        return s == null || s.isEmpty()|| s.isBlank();
    }
}
