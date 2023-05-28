package com.eleks.placescanner.place.domain.demographic.precisaly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
@JsonIgnoreProperties(ignoreUnknown = true)
public record Boundaries(ArrayList<Boundary> boundary){
}
