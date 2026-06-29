package com.trading.trading.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderClass {
    @JsonProperty("equity")   EQUITY,
    @JsonProperty("option")   OPTION,
    @JsonProperty("combo")    COMBO,
    @JsonProperty("multileg") MULTILEG
}
