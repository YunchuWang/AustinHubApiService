package com.austinhub.apiservice.model.dto;

import com.austinhub.apiservice.model.enums.ItemType;
import com.austinhub.apiservice.model.enums.PricingPlan;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewOrderItemDTO implements OrderItemDTO {
    private ItemType itemType;
    private PricingPlan pricingPlan;
    // resource or membership id
    private Integer itemId;
}
