package com.trading.accounts.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Margin sub-record within an account balance.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Margin(
        @JsonProperty("fed_call")              double fedCall,
        @JsonProperty("maintenance_call")      double maintenanceCall,
        @JsonProperty("option_buying_power")   double optionBuyingPower,
        @JsonProperty("stock_buying_power")    double stockBuyingPower,
        @JsonProperty("stock_short_value")     double stockShortValue,
        @JsonProperty("sweep")                 double sweep
) {}
