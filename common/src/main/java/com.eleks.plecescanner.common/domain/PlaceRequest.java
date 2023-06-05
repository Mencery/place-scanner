package com.eleks.plecescanner.common.domain;

public record PlaceRequest(
        String placeName,
        String state,
        String zipCode
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
        return placeName == null || placeName.isEmpty()|| placeName.isBlank();
    }
}
