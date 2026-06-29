package com.trading.market.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * A single OHLCV bar from {@code GET /v1/markets/history}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record HistoricalQuote(
        @JsonProperty("date")   LocalDate date,
        @JsonProperty("open")   double    open,
        @JsonProperty("high")   double    high,
        @JsonProperty("low")    double    low,
        @JsonProperty("close")  double    close,
        @JsonProperty("volume") long      volume
) {}
