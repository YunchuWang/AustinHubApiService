package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.po.Ads;
import com.austinhub.apiservice.model.po.Order;
import com.austinhub.apiservice.service.OrderService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/owned")
    public ResponseEntity<List<Order>> findOwnedOrders(
            @Valid @NotNull @RequestParam String accountName) {
        return ResponseEntity.ok().body(orderService.findOwnedOrders(accountName));
    }
}
