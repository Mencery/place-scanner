package com.eleks.placescanner.common.domain.promaptools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PromaptoolsResponse(int status, @JsonProperty("output") List<Output> outputList) {
}
