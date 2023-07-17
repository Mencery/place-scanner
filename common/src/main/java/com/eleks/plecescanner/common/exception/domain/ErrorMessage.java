package com.eleks.plecescanner.common.exception.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessage {
    @JsonProperty("statusCode") private int statusCode;
    @JsonProperty("timestamp") private Date timestamp;
    @JsonProperty("message") private String message;
    @JsonProperty("description") private String description;
}
