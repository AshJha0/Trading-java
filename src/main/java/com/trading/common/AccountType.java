package com.trading.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The margin type of a tradingbrokertestage account. */
public enum AccountType {
    @JsonProperty("cash")
    CASH,

    @JsonProperty("margin")
    MARGIN
}
