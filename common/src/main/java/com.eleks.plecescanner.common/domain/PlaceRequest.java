package com.eleks.plecescanner.common.domain;

public record PlaceRequest(
        String placeName,
        String state
) {

    public PlaceRequest validateRequest(){
        if(isEmptyString(placeName)){
            throw new IllegalStateException("placeName cannot be empty");
        }
        if (isEmptyString(state)){
            throw new IllegalStateException("state cannot be empty");
        }
        return this;
    }

    private boolean isEmptyString(String s){
        return placeName == null || placeName.isEmpty()|| placeName.isBlank();
    }
}
