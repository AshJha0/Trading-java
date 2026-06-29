package com.trading.accounts.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An open position in an account.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Position(
        @JsonProperty("cost_basis")     double costBasis,
        @JsonProperty("date_acquired")  String dateAcquired,
        @JsonProperty("id")             long   id,
        @JsonProperty("quantity")       double quantity,
        @JsonProperty("symbol")         String symbol
) {}
