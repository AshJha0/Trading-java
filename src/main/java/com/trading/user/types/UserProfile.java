package com.trading.user.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * User profile returned by GET /user/profile.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserProfile(
        @JsonProperty("id")      String            id,
        @JsonProperty("name")    String            name,
        @JsonProperty("account") List<UserAccount> accounts
) {}
