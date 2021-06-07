package com.austinhub.apiservice.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakePaymentDto {

    private BigDecimal transactionAmount;
    private String nonce;
}
