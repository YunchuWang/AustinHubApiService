package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.AdsDTO;
import com.austinhub.apiservice.model.dto.BoothDTO;
import com.austinhub.apiservice.model.dto.JobDTO;
import com.austinhub.apiservice.model.dto.MembershipDTO;
import com.austinhub.apiservice.model.dto.OrderDTO;
import com.austinhub.apiservice.model.dto.OrderItemDTO;
import com.austinhub.apiservice.model.enums.OrderStatus;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Order;
import com.austinhub.apiservice.repository.AccountRepository;
import com.austinhub.apiservice.repository.OrderRepository;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private AccountRepository accountRepository;
    private AdsService adsService;
    private JobsService jobsService;
    private BoothService boothService;
    private MembershipService membershipService;

    public Order saveOrder(@NonNull OrderDTO orderDto) {
        // create order first
        // get account
        final Account account = accountRepository.findByUsername(orderDto.getAccountName());
        final Order order = Order.builder()
                .account(account)
                .orderNumber(UUID.randomUUID().toString())
                .price(orderDto.getPrice())
                .status(OrderStatus.REQUESTED)
                .build();
        System.out.println(order.toString());
        final Order createdOrder = orderRepository.save(order);
        System.out.println(createdOrder.toString());
        System.out.println(orderDto.toString());
        final Date orderCreatedTimestamp = createdOrder.getCreatedTimestamp();
        final int orderId = createdOrder.getId();

        // for each order item
        for (OrderItemDTO orderItemDTO : orderDto.getOrderItems()) {
            IOrderItemSaveService orderItemSaveService = this.getOrderItemSaveService(orderItemDTO);
            orderItemSaveService
                    .save(orderItemDTO, account, orderCreatedTimestamp, orderId,
                            null, orderItemDTO.getItemType().name().toLowerCase());
        }

        return createdOrder;
    }

    public void updateOrder(Order order) {
        this.orderRepository.save(order);
    }

    public IOrderItemSaveService getOrderItemSaveService(OrderItemDTO orderItemDTO) {
        if (orderItemDTO instanceof AdsDTO) {
            return adsService;
        } else if (orderItemDTO instanceof BoothDTO) {
            return boothService;
        } else if (orderItemDTO instanceof JobDTO) {
            return jobsService;
        } else if (orderItemDTO instanceof MembershipDTO) {
            return membershipService;
        }
        throw new RuntimeException("Invalid order item type!");
    }
}
