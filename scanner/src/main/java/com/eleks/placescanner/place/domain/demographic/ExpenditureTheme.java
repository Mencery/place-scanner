package com.eleks.placescanner.place.domain.demographic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExpenditureTheme(
         String boundaryRef,
         ArrayList<IndividualValueVariable> individualValueVariable,
         ArrayList<RangeVariable> rangeVariable
){
}
