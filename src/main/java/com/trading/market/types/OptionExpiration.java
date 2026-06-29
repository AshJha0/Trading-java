package com.trading.market.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A single expiration entry from {@code GET /v1/markets/options/expirations}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OptionExpiration(
        @JsonProperty("date")            String      date,
        @JsonProperty("contract_size")   Long        contractSize,
        @JsonProperty("expiration_type") String      expirationType,
        @JsonProperty("strikes")         List<Double> strikes
) {}
