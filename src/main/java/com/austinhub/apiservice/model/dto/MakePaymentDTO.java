package com.austinhub.apiservice.model.dto;

import com.austinhub.apiservice.deserializers.MoneyDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakePaymentDTO {
    @JsonDeserialize(using= MoneyDeserializer.class)
    private BigDecimal transactionAmount;
    private String nonce;
    @JsonProperty("orderInfo")
    private OrderDTO orderDTO;
}
