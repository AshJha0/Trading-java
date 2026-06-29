package com.trading.common;

/** Direction used for sorting paginated endpoint results. */
public enum SortOrder {
    ASC,
    DESC;

    /** Returns the lowercase wire string expected by the TradingBrokerTest API. */
    public String toWire() {
        return name().toLowerCase();
    }
}
