package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Order;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order, Integer> {

    List<Order> findByAccountId(Integer accountId);
}
