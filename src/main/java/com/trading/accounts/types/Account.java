package com.trading.accounts.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A TradingBrokerTest tradingbrokertestage account summary.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Account(
        @JsonProperty("account_number")   String  accountNumber,
        @JsonProperty("classification")   String  classification,
        @JsonProperty("date_created")     String  dateCreated,
        @JsonProperty("day_trader")       boolean dayTrader,
        @JsonProperty("option_level")     int     optionLevel,
        @JsonProperty("status")           String  status,
        @JsonProperty("type")             String  type,
        @JsonProperty("last_update_date") String  lastUpdateDate
) {}
