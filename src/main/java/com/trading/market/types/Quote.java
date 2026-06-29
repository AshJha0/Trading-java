package com.trading.market.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single quote row returned by {@code GET|POST /v1/markets/quotes}.
 *
 * <p>All numeric fields are {@code Double} / {@code Long} (nullable) because
 * TradingBrokerTest may omit them for certain security types.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Quote(
        @JsonProperty("symbol")            String  symbol,
        @JsonProperty("description")       String  description,
        @JsonProperty("exch")              String  exch,
        @JsonProperty("type")              String  quoteType,
        @JsonProperty("last")              Double  last,
        @JsonProperty("change")            Double  change,
        @JsonProperty("volume")            Long    volume,
        @JsonProperty("open")              Double  open,
        @JsonProperty("high")              Double  high,
        @JsonProperty("low")               Double  low,
        @JsonProperty("close")             Double  close,
        @JsonProperty("bid")               Double  bid,
        @JsonProperty("ask")               Double  ask,
        @JsonProperty("change_percentage") Double  changePercentage,
        @JsonProperty("average_volume")    Long    averageVolume,
        @JsonProperty("last_volume")       Long    lastVolume,
        @JsonProperty("trade_date")        Long    tradeDate,
        @JsonProperty("prevclose")         Double  prevclose,
        @JsonProperty("week_52_high")      Double  week52High,
        @JsonProperty("week_52_low")       Double  week52Low,
        @JsonProperty("bidsize")           Long    bidsize,
        @JsonProperty("bidexch")           String  bidexch,
        @JsonProperty("bid_date")          Long    bidDate,
        @JsonProperty("asksize")           Long    asksize,
        @JsonProperty("askexch")           String  askexch,
        @JsonProperty("ask_date")          Long    askDate,
        @JsonProperty("root_symbols")      String  rootSymbols,
        // Option-specific fields
        @JsonProperty("underlying")        String  underlying,
        @JsonProperty("strike")            Double  strike,
        @JsonProperty("open_interest")     Long    openInterest,
        @JsonProperty("contract_size")     Long    contractSize,
        @JsonProperty("expiration_date")   String  expirationDate,
        @JsonProperty("expiration_type")   String  expirationType,
        @JsonProperty("option_type")       String  optionType,
        @JsonProperty("root_symbol")       String  rootSymbol,
        @JsonProperty("greeks")            GreeksData greeks
) {}
