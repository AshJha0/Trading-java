package com.trading.market.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Option Greeks returned when the {@code greeks=true} query parameter is passed
 * to the quotes endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GreeksData(
        @JsonProperty("delta")      Double delta,
        @JsonProperty("gamma")      Double gamma,
        @JsonProperty("theta")      Double theta,
        @JsonProperty("vega")       Double vega,
        @JsonProperty("rho")        Double rho,
        @JsonProperty("phi")        Double phi,
        @JsonProperty("bid_iv")     Double bidIv,
        @JsonProperty("mid_iv")     Double midIv,
        @JsonProperty("ask_iv")     Double askIv,
        @JsonProperty("smv_vol")    Double smvVol,
        @JsonProperty("updated_at") String updatedAt
) {}
