package com.austinhub.apiservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakePaymentDTO {
    private BigDecimal transactionAmount;
    private String nonce;
    @JsonProperty("orderInfo")
    private OrderDTO orderDto;
}
