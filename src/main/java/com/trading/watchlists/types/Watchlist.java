package com.trading.watchlists.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A named watchlist containing tracked symbols.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Watchlist(
        @JsonProperty("id")        String              id,
        @JsonProperty("name")      String              name,
        @JsonProperty("public_id") String              publicId,
        @JsonProperty("items")     List<WatchlistItem> items
) {}
