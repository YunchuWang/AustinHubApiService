package com.austinhub.apiservice.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Getter
public enum MembershipType {
    @JsonProperty("Basic")
    BASIC("basic"),
    @JsonProperty("Advanced")
    ADVANCED("advanced"),
    ;

    private String type;
    MembershipType(String type) {
        this.type = type;
    }
}
