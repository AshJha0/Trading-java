package com.trading.market.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single time-and-sales entry from {@code GET /v1/markets/timesales}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TimeSale(
        @JsonProperty("time")      String time,
        @JsonProperty("timestamp") long   timestamp,
        @JsonProperty("price")     double price,
        @JsonProperty("open")      double open,
        @JsonProperty("high")      double high,
        @JsonProperty("low")       double low,
        @JsonProperty("close")     double close,
        @JsonProperty("volume")    long   volume,
        @JsonProperty("vwap")      Double vwap
) {}
