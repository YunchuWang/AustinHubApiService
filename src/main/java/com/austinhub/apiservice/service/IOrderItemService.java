package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.PlaceOrderItemDTO;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Order;
import java.util.Date;

public interface IOrderItemService {

    public void save(PlaceOrderItemDTO placeOrderItemDTO, Account account, Date createdTimestamp,
            Order order, Integer membershipId, String resourceTypeName);
}
