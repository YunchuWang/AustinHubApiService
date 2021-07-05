package com.austinhub.apiservice.model.dto;

import com.austinhub.apiservice.model.enums.OrderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "orderType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlaceOrderDTO.class, name = "NEW"),
        @JsonSubTypes.Type(value = RenewOrderDTO.class, name = "RENEW"),
}
)
public abstract class OrderDTO {

    private OrderType orderType;
}