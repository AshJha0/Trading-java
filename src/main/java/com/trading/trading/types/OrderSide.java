package com.trading.trading.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderSide {
    @JsonProperty("buy")          BUY,
    @JsonProperty("sell")         SELL,
    @JsonProperty("buy_to_cover") BUY_TO_COVER,
    @JsonProperty("sell_short")   SELL_SHORT,
    @JsonProperty("buy_to_open")  BUY_TO_OPEN,
    @JsonProperty("buy_to_close") BUY_TO_CLOSE,
    @JsonProperty("sell_to_open") SELL_TO_OPEN,
    @JsonProperty("sell_to_close")SELL_TO_CLOSE
}
