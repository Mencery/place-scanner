package com.eleks.placescanner.place.domain.demographic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
@JsonIgnoreProperties(ignoreUnknown = true)
public record RangeVariable(String count,
                            String order,
                            String name,
                            String alias,
                            String description,
                            String baseVariable,
                            String year,
                            ArrayList<Field> field ){
}
