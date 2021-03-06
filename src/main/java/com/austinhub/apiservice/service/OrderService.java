package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.CreateAdsDTO;
import com.austinhub.apiservice.model.dto.CreateBoothDTO;
import com.austinhub.apiservice.model.dto.CreateJobDTO;
import com.austinhub.apiservice.model.dto.CreateMembershipDTO;
import com.austinhub.apiservice.model.dto.PlaceOrderDTO;
import com.austinhub.apiservice.model.dto.PlaceOrderItemDTO;
import com.austinhub.apiservice.model.dto.RenewOrderDTO;
import com.austinhub.apiservice.model.dto.RenewOrderItemDTO;
import com.austinhub.apiservice.model.enums.ItemType;
import com.austinhub.apiservice.model.enums.OrderStatus;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Order;
import com.austinhub.apiservice.repository.AccountRepository;
import com.austinhub.apiservice.repository.OrderRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private AccountRepository accountRepository;
    private AdsService adsService;
    private ResourceService resourceService;
    private JobsService jobsService;
    private BoothService boothService;
    private MembershipService membershipService;

    public Order saveOrder(@NonNull PlaceOrderDTO placeOrderDto) {
        // create order first
        // get account
        final Account account = accountRepository.findByUsername(placeOrderDto.getAccountName());
        final Order order = Order.builder()
                                 .account(account)
                                 .orderNumber(UUID.randomUUID().toString().replaceAll("-", ""))
                                 .price(placeOrderDto.getPrice())
                                 .status(OrderStatus.REQUESTED)
                                 .build();
        final Order createdOrder = orderRepository.save(order);
        final Date orderCreatedTimestamp = createdOrder.getCreatedTimestamp();
        final int orderId = createdOrder.getId();

        // for each order item
        for (PlaceOrderItemDTO placeOrderItemDTO : placeOrderDto.getOrderItems()) {
            IOrderItemService orderItemSaveService = this.getOrderItemService(
                    placeOrderItemDTO);
            orderItemSaveService
                    .save(placeOrderItemDTO, account, orderCreatedTimestamp, order,
                          null, placeOrderItemDTO.getItemType().name().toLowerCase());
        }

        return createdOrder;
    }

    public Order renewOrder(@NonNull RenewOrderDTO renewOrderDTO) {
        // create order first
        // get account
        final Account account = accountRepository.findByUsername(renewOrderDTO.getAccountName());
        final Order order = Order.builder()
                                 .account(account)
                                 .orderNumber(UUID.randomUUID().toString().replaceAll("-", ""))
                                 .price(renewOrderDTO.getPrice())
                                 .status(OrderStatus.REQUESTED)
                                 .build();
        final Order createdOrder = orderRepository.save(order);

        // only this part diff from place order, item already exists just need update
        // expiration timestamp
        // for each order item
        for (RenewOrderItemDTO renewOrderItemDTO : renewOrderDTO.getOrderItems()) {
            if (renewOrderItemDTO.getItemType().equals(ItemType.MEMBERSHIP)) {
                membershipService.renew(renewOrderItemDTO, order);
            } else {
                resourceService.renew(renewOrderItemDTO, order);
            }
        }

        return createdOrder;
    }


    public void updateOrder(Order order) {
        this.orderRepository.save(order);
    }

    public IOrderItemService getOrderItemService(PlaceOrderItemDTO orderItemDTO) {
        if (orderItemDTO instanceof CreateAdsDTO) {
            return adsService;
        } else if (orderItemDTO instanceof CreateBoothDTO) {
            return boothService;
        } else if (orderItemDTO instanceof CreateJobDTO) {
            return jobsService;
        } else if (orderItemDTO instanceof CreateMembershipDTO) {
            return membershipService;
        }
        throw new RuntimeException("Invalid order item type!");
    }

    public List<Order> findOwnedOrders(String accountName) {
        Account account = accountRepository.findByUsername(accountName);
        if (account == null) {
            throw new RuntimeException(String.format("Account %s does not exist", accountName));
        }
        return orderRepository.findByAccountId(account.getId());
    }
}
