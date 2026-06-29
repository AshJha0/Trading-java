package com.trading.user.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Account summary within a user profile.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserAccount(
        @JsonProperty("account_number") String  accountNumber,
        @JsonProperty("classification") String  classification,
        @JsonProperty("day_trader")     boolean dayTrader,
        @JsonProperty("option_level")   int     optionLevel,
        @JsonProperty("status")         String  status,
        @JsonProperty("type")           String  type
) {}
