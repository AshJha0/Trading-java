package com.trading.accounts.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Account balance details returned by GET /accounts/{id}/balances.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountBalance(
        @JsonProperty("option_short_value")    double  optionShortValue,
        @JsonProperty("total_equity")          double  totalEquity,
        @JsonProperty("account_number")        String  accountNumber,
        @JsonProperty("account_type")          String  accountType,
        @JsonProperty("close_pl")              double  closePl,
        @JsonProperty("current_requirement")   double  currentRequirement,
        @JsonProperty("equity")                double  equity,
        @JsonProperty("equity_percent")        double  equityPercent,
        @JsonProperty("long_market_value")     double  longMarketValue,
        @JsonProperty("market_value")          double  marketValue,
        @JsonProperty("open_pl")               double  openPl,
        @JsonProperty("option_long_value")     double  optionLongValue,
        @JsonProperty("option_requirement")    double  optionRequirement,
        @JsonProperty("pending_orders_count")  int     pendingOrders,
        @JsonProperty("short_market_value")    double  shortMarketValue,
        @JsonProperty("stock_long_value")      double  stockLongValue,
        @JsonProperty("total_cash")            double  totalCash,
        @JsonProperty("uncleared_funds")       double  uncleared,
        @JsonProperty("pending_cash")          double  pendingCash,
        @JsonProperty("margin")                Margin  margin
) {}
