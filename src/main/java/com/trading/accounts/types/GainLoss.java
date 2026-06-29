package com.trading.accounts.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A closed position gain/loss record.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GainLoss(
        @JsonProperty("close_date")        String closeDate,
        @JsonProperty("cost")              double cost,
        @JsonProperty("gain_loss")         double gainLoss,
        @JsonProperty("gain_loss_percent") double gainLossPercent,
        @JsonProperty("open_date")         String openDate,
        @JsonProperty("proceeds")          double proceeds,
        @JsonProperty("quantity")          double quantity,
        @JsonProperty("symbol")            String symbol,
        @JsonProperty("term")              int    term
) {}
