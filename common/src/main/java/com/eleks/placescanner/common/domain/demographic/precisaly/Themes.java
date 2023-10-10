package com.eleks.placescanner.common.domain.demographic.precisaly;

import com.eleks.placescanner.common.domain.demographic.precisaly.theme.Theme;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Themes(
        @JsonProperty(value = "populationTheme") Theme populationTheme,
        @JsonProperty(value = "raceAndEthnicityTheme") Theme raceAndEthnicityTheme,
        @JsonProperty(value = "expenditureTheme") Theme expenditureTheme,
        @JsonProperty(value = "housingTheme") Theme housingTheme,
        @JsonProperty(value = "incomeTheme") Theme incomeTheme,
        @JsonProperty(value = "assetsAndWealthTheme") Theme assetsAndWealthTheme) {
}
