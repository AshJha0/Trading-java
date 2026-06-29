package com.trading.trading.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderDuration {
    @JsonProperty("day")  DAY,
    @JsonProperty("gtc")  GTC,
    @JsonProperty("pre")  PRE,
    @JsonProperty("post") POST
}
