package com.eleks.placescanner.common.domain.demographic.precisaly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PreciselyToken(
        @JsonProperty(value = "access_token") String accessToken,
        String tokenType,
        String issuedAt,
        String expiresIn,
        @JsonProperty(value = "clientID") String clientId,
        String org) {
}
