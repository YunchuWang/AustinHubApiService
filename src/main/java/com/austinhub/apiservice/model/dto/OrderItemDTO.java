package com.austinhub.apiservice.model.dto;

import com.austinhub.apiservice.model.enums.ItemType;
import com.austinhub.apiservice.model.enums.PricingPlan;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "itemType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JobDTO.class, name = "JOB"),
        @JsonSubTypes.Type(value = AdsDTO.class, name = "ADS"),
        @JsonSubTypes.Type(value = BoothDTO.class, name = "BOOTH"),
        @JsonSubTypes.Type(value = MembershipDTO.class, name = "MEMBERSHIP")
}
)
public abstract class OrderItemDTO {
    private ItemType itemType;
    private PricingPlan pricingPlan;
}
