package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.MakePaymentDto;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/payment")
public class PaymentController {

    private BraintreeGateway braintreeGateway;

    public PaymentController(BraintreeGateway braintreeGateway) {
        this.braintreeGateway = braintreeGateway;
    }

    @PostMapping("/make")
    public ResponseEntity<Result<Transaction>> makePayment(
            @Valid @NotNull @RequestBody MakePaymentDto makePaymentDto) {
        // save order to db: OPEN
        // make payment call
        TransactionRequest request = new TransactionRequest()
                .amount(makePaymentDto.getTransactionAmount())
                .paymentMethodNonce(makePaymentDto.getNonce())
                .options()
                .submitForSettlement(true)
                .done();
        Result<Transaction> transaction = braintreeGateway.transaction().sale(request);
        // server crash
        // determine status
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/client_token")
    public ResponseEntity<Map<String, String>> getClientToken() {
        return ResponseEntity
                .ok(ImmutableMap.of("client_token", braintreeGateway.clientToken().generate()));
    }
}