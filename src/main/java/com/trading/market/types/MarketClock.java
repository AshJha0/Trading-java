package com.trading.market.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Market clock status from {@code GET /v1/markets/clock}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MarketClock(
        @JsonProperty("date")        String date,
        @JsonProperty("description") String description,
        @JsonProperty("state")       String state,
        @JsonProperty("timestamp")   long   timestamp,
        @JsonProperty("next_change") String nextChange,
        @JsonProperty("next_state")  String nextState
) {}
