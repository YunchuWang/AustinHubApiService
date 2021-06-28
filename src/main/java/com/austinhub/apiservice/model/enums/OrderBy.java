package com.austinhub.apiservice.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderBy {
    @JsonProperty("CREATED_TIMESTAMP")
    CREATED_TIMESTAMP,
    @JsonProperty("TITLE")
    TITLE
}
