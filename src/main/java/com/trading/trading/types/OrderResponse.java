package com.trading.trading.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response returned when placing, modifying, or cancelling an order.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderResponse(
        @JsonProperty("id")         long   id,
        @JsonProperty("status")     String status,
        @JsonProperty("partner_id") String partnerId
) {}
