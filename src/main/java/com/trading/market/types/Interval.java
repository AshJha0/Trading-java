package com.trading.market.types;

/** Historical data bar granularity for the {@code /v1/markets/history} endpoint. */
public enum Interval {
    DAILY,
    WEEKLY,
    MONTHLY;

    /** Returns the lowercase wire string expected by the TradingBrokerTest API. */
    public String toWire() {
        return name().toLowerCase();
    }
}
