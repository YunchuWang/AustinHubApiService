package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.MakePaymentDTO;
import com.austinhub.apiservice.model.enums.OrderStatus;
import com.austinhub.apiservice.model.po.Order;
import com.austinhub.apiservice.service.OrderService;
import com.austinhub.apiservice.service.TransactionService;
import com.austinhub.apiservice.utils.ApplicationUtils;
import com.austinhub.apiservice.utils.GsonUtils;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.TransactionRequest;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/payment")
public class PaymentController {

    private BraintreeGateway braintreeGateway;
    private OrderService orderService;
    private TransactionService transactionService;

    public PaymentController(BraintreeGateway braintreeGateway,
            OrderService orderService,
            TransactionService transactionService) {
        this.braintreeGateway = braintreeGateway;
        this.orderService = orderService;
        this.transactionService = transactionService;
    }


    @PostMapping("/make")
    @Transactional
    public ResponseEntity<Map<String, String>> makePayment(
            @Valid @NotNull @RequestBody MakePaymentDTO makePaymentDto) {
        // save order to db: OPEN
        System.out.println(makePaymentDto.toString());
        Order createdOrder = orderService.saveOrder(makePaymentDto.getOrderDto());
        // make payment call
        TransactionRequest request = new TransactionRequest()
                .amount(makePaymentDto.getTransactionAmount())
                .paymentMethodNonce(makePaymentDto.getNonce())
                .options()
                .storeInVault(true)
                .submitForSettlement(true)
                .done();
        Result<Transaction> transaction = braintreeGateway.transaction().sale(request);

        System.out.println(GsonUtils.getGson().toJson(transaction));
        // persistent transaction
        Objects.requireNonNull(transaction.getTarget(), "Transaction is empty!");
        final Timestamp createdTimestamp = new Timestamp(
                transaction.getTarget().getCreatedAt().getTimeInMillis());
        final BigDecimal transactionAmount = transaction.getTarget().getAmount();
        final String transactionType = transaction.getTarget().getType().name();
        final String transactionId = transaction.getTarget().getId();
        final String merchantId = transaction.getTarget().getMerchantAccountId();
        final Transaction.Status status = transaction.getTarget().getStatus();
        com.austinhub.apiservice.model.po.Transaction createdTransaction = transactionService
                .saveTransaction(com.austinhub.apiservice.model.po.Transaction.builder()
                        .orderId(createdOrder.getId())
                        .amount(transactionAmount)
                        .createdTimestamp(createdTimestamp)
                        .expiryTime(ApplicationUtils
                                .calculateTransactionExpirationTimestamp(1, createdTimestamp))
                        .type(transactionType)
                        .externalId(transactionId)
                        .merchantId(merchantId)
                        .status(status)
                        .build());
        this.transactionService.saveTransaction(createdTransaction);

        if (createdTransaction.getStatus().equals(Status.SETTLED) || createdTransaction.getStatus()
                .equals(Status.SUBMITTED_FOR_SETTLEMENT)) {
            createdOrder.setStatus(OrderStatus.COMPLETED);
        } else {
            createdOrder.setStatus(OrderStatus.DECLINED);
        }
        this.orderService.updateOrder(createdOrder);

        if (OrderStatus.COMPLETED.equals(createdOrder.getStatus())) {
            // TODO: send order confirm email
            return ResponseEntity.ok(ImmutableMap
                    .of("message", "Order placed!", "orderNo", createdOrder.getOrderNumber()));
        } else {
            // specify err
            return ResponseEntity.badRequest()
                    .body(ImmutableMap.of("message", transaction.getMessage()));
        }
    }

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