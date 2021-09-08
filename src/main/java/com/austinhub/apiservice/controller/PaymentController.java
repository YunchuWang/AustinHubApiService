package com.austinhub.apiservice.controller;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private BraintreeGateway braintreeGateway;

    @PostMapping("/client_token")
    public ResponseEntity<Map<String, String>> getClientToken(
            @RequestBody(required = false) String customerId) {
        if (customerId != null) {
            final ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                    .customerId(customerId);
            return ResponseEntity
                    .ok(ImmutableMap.of("client_token",
                            braintreeGateway.clientToken().generate(clientTokenRequest)));
        }
        return ResponseEntity
                .ok(ImmutableMap.of("client_token", braintreeGateway.clientToken().generate()));
    }
}
