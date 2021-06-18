package com.austinhub.apiservice.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty("REQUESTED")
    REQUESTED,
    @JsonProperty("DECLINED")
    DECLINED,
    @JsonProperty("CANCELLED")
    CANCELLED,
    @JsonProperty("COMPLETED")
    COMPLETED
}
