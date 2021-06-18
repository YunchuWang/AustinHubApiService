package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.OrderItemDTO;
import com.austinhub.apiservice.model.po.Account;
import java.sql.Timestamp;
import java.util.Date;

public interface IOrderItemSaveService {

    public void save(OrderItemDTO orderItemDTO, Account account, Date createdTimestamp,
            Integer orderId, Integer membershipId, String resourceTypeName);
}
