package com.trading.watchlists.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single item (symbol) within a watchlist.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WatchlistItem(
        @JsonProperty("id")           long   id,
        @JsonProperty("watchlist_id") String watchlistId,
        @JsonProperty("symbol")       String symbol,
        @JsonProperty("added_date")   String addedDate
) {}
