package com.austinhub.apiservice.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderType {
    @JsonProperty("NEW")
    NEW,
    @JsonProperty("RENEW")
    RENEW,
}
