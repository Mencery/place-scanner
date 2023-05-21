package com.eleks.placescanner.place.domain.demographic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
@JsonIgnoreProperties(ignoreUnknown = true)
public record Boundaries(ArrayList<Boundary> boundary){
}
