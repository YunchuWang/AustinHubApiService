package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order, Integer> {
}
