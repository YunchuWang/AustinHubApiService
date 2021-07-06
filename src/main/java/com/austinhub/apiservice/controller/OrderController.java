package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.MakePaymentDTO;
import com.austinhub.apiservice.model.dto.OrderDTO;
import com.austinhub.apiservice.model.dto.PlaceOrderDTO;
import com.austinhub.apiservice.model.dto.RenewOrderDTO;
import com.austinhub.apiservice.model.enums.ItemType;
import com.austinhub.apiservice.model.enums.OrderStatus;
import com.austinhub.apiservice.model.enums.OrderType;
import com.austinhub.apiservice.model.po.Order;
import com.austinhub.apiservice.service.MailService;
import com.austinhub.apiservice.service.MembershipService;
import com.austinhub.apiservice.service.OrderService;
import com.austinhub.apiservice.service.ResourceService;
import com.austinhub.apiservice.service.TransactionService;
import com.austinhub.apiservice.utils.ApplicationUtils;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.TransactionRequest;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Validated
@RequestMapping("/orders")
public class OrderController {

    private final BraintreeGateway braintreeGateway;
    private final OrderService orderService;
    private final ResourceService resourceService;
    private final MembershipService membershipService;
    private final MailService mailService;
    private final TransactionService transactionService;
    private final KafkaTemplate kafkaTemplate;

    @GetMapping("/owned")
    public ResponseEntity<List<Order>> findOwnedOrders(
            @Valid @NotNull @RequestParam String accountName) {
        return ResponseEntity.ok().body(orderService.findOwnedOrders(accountName));
    }

    @PostMapping("/make")
    @Transactional
    public ResponseEntity<Map<String, String>> placeOrder(
            @Valid @NotNull @RequestBody MakePaymentDTO makePaymentDTO) {
        final Order order = orderService.saveOrder((PlaceOrderDTO) makePaymentDTO.getOrderDTO());
        Result<Transaction> transaction = makePayment(
                makePaymentDTO.getTransactionAmount(), makePaymentDTO.getNonce());
        final Order updatedOrder = executeOrder(OrderType.NEW,
                                                order,
                                                transaction);
        return takeActionPostOrder(updatedOrder, transaction,
                                   makePaymentDTO.getOrderDTO());
    }

    @PostMapping("/renew")
    @Transactional
    public ResponseEntity<Map<String, String>> renewOrder(
            @Valid @NotNull @RequestBody MakePaymentDTO makePaymentDTO) {
        final Order order = orderService.renewOrder((RenewOrderDTO) makePaymentDTO.getOrderDTO());
        Result<Transaction> transaction = makePayment(
                makePaymentDTO.getTransactionAmount(), makePaymentDTO.getNonce());
        final Order updatedOrder = executeOrder(OrderType.RENEW,
                                                order,
                                                transaction);
        return takeActionPostOrder(updatedOrder, transaction,
                                   makePaymentDTO.getOrderDTO());
    }

    @org.jetbrains.annotations.NotNull
    private Order executeOrder(
            OrderType orderType, Order createdOrder,
            Result<Transaction> transaction) {

        // persistent transaction
        com.austinhub.apiservice.model.po.Transaction createdTransaction = getTransaction(
                createdOrder,
                transaction);
        this.transactionService.saveTransaction(createdTransaction);

        Status status = createdTransaction.getStatus();
        if (status.equals(Status.SETTLED)
                || com.austinhub.apiservice.model.po.Transaction.IN_PROGRESS_STATUS
                .contains(status)) {
            createdOrder.setStatus(OrderStatus.COMPLETED);
        } else if (com.austinhub.apiservice.model.po.Transaction.DECLINED_STATUS.contains(status)) {
            createdOrder.setStatus(OrderStatus.DECLINED);
        }
        this.orderService.updateOrder(createdOrder);
        return createdOrder;
    }

    @org.jetbrains.annotations.NotNull
    private ResponseEntity<Map<String, String>> takeActionPostOrder(Order createdOrder,
            Result<Transaction> transaction, OrderDTO orderDTO) {
        if (OrderStatus.COMPLETED.equals(createdOrder.getStatus())) {
            // if renew order, extend expiration time
            if (orderDTO.getOrderType().equals(OrderType.RENEW)) {
                RenewOrderDTO renewOrderDTO = (RenewOrderDTO) orderDTO;
                if (!renewOrderDTO.getOrderItems().isEmpty()) {
                    renewOrderDTO.getOrderItems().forEach(orderItem -> {
                        if (orderItem.getItemType().equals(ItemType.MEMBERSHIP)) {
                            this.membershipService.extendExpiration(orderItem);
                        } else {
                            this.resourceService.extendExpiration(orderItem);
                        }
                    });
                }
            }

            // Send email
            final String accountEmail = createdOrder.getAccount().getEmail();
            kafkaTemplate.send("email", mailService.constructOrderConfirmationEmail(accountEmail,
                                                                                    createdOrder
                                                                                            .getOrderNumber()));
            return ResponseEntity.ok(
                    ImmutableMap.of(
                            "message", "Order placed!",
                            "orderNo", createdOrder.getOrderNumber(),
                            "customerId", transaction.getTarget().getCustomer().getId()
                    ));
        } else {
            // specify err
            return ResponseEntity.badRequest()
                                 .body(ImmutableMap.of("message", transaction.getMessage()));
        }
    }

    private Result<Transaction> makePayment(BigDecimal transactionAmount, String nonce) {
        // make payment call
        TransactionRequest request = new TransactionRequest()
                .amount(transactionAmount)
                .paymentMethodNonce(nonce)
                .options()
                .storeInVault(true)
                .submitForSettlement(true)
                .done();
        return braintreeGateway.transaction().sale(request);
    }

    private com.austinhub.apiservice.model.po.Transaction getTransaction(Order createdOrder,
            Result<Transaction> transaction) {
        Objects.requireNonNull(transaction.getTarget(), "Transaction is empty!");
        final Timestamp createdTimestamp = new Timestamp(
                transaction.getTarget().getCreatedAt().getTimeInMillis());
        final BigDecimal settledTransactionAmount = transaction.getTarget().getAmount();
        final String transactionType = transaction.getTarget().getType().name();
        final String transactionId = transaction.getTarget().getId();
        final String merchantId = transaction.getTarget().getMerchantAccountId();
        final Status status = transaction.getTarget().getStatus();
        com.austinhub.apiservice.model.po.Transaction createdTransaction = transactionService
                .saveTransaction(com.austinhub.apiservice.model.po.Transaction.builder()
                                                                              .orderId(createdOrder
                                                                                               .getId())
                                                                              .amount(settledTransactionAmount)
                                                                              .createdTimestamp(
                                                                                      createdTimestamp)
                                                                              .expiryTime(
                                                                                      ApplicationUtils
                                                                                              .calculateTransactionExpirationTimestamp(
                                                                                                      1,
                                                                                                      createdTimestamp))
                                                                              .type(transactionType)
                                                                              .externalId(
                                                                                      transactionId)
                                                                              .merchantId(
                                                                                      merchantId)
                                                                              .status(status)
                                                                              .build());
        return createdTransaction;
    }
}
