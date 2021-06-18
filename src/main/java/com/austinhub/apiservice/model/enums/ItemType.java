package com.austinhub.apiservice.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ItemType {
    @JsonProperty("JOB")
    JOB,
    @JsonProperty("ADS")
    ADS,
    @JsonProperty("BOOTH")
    BOOTH,
    @JsonProperty("MEMBERSHIP")
    MEMBERSHIP;
}
