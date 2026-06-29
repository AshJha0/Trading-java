package com.trading.market.types;

/** Bar granularity for the {@code /v1/markets/timesales} endpoint. */
public enum TimeSalesInterval {
    TICK("tick"),
    ONE_MINUTE("1min"),
    FIVE_MINUTES("5min"),
    FIFTEEN_MINUTES("15min");

    private final String wire;

    TimeSalesInterval(String wire) {
        this.wire = wire;
    }

    /** Returns the wire string expected by the TradingBrokerTest API (e.g. {@code "1min"}). */
    public String toWire() {
        return wire;
    }
}
