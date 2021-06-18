package com.austinhub.apiservice.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PricingPlan {
    @JsonProperty("MONTHLY")
    MONTHLY(1),
    @JsonProperty("QUARTERLY")
    QUARTERLY(3),
    @JsonProperty("YEARLY")
    YEARLY(12);

    private int numberOfMonths;

    PricingPlan(int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }
}
