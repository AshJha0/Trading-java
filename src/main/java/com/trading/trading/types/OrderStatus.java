package com.trading.trading.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty("pending")          PENDING,
    @JsonProperty("open")             OPEN,
    @JsonProperty("partially_filled") PARTIALLY_FILLED,
    @JsonProperty("filled")           FILLED,
    @JsonProperty("cancelled")        CANCELLED,
    @JsonProperty("canceled")         CANCELED,
    @JsonProperty("rejected")         REJECTED,
    @JsonProperty("expired")          EXPIRED,
    @JsonProperty("pending_cancel")   PENDING_CANCEL,
    @JsonProperty("error")            ERROR
}
