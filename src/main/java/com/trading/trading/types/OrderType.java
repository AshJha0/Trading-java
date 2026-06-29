package com.trading.trading.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderType {
    @JsonProperty("market")     MARKET,
    @JsonProperty("limit")      LIMIT,
    @JsonProperty("stop")       STOP,
    @JsonProperty("stop_limit") STOP_LIMIT,
    @JsonProperty("debit")      DEBIT,
    @JsonProperty("credit")     CREDIT,
    @JsonProperty("even")       EVEN
}
