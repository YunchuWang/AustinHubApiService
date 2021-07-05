package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.enums.OrderStatus;
import com.austinhub.apiservice.model.po.Order;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order, Integer> {

    List<Order> findByAccountId(Integer accountId);

    @Modifying
    @Query(value =
            "update Order o set o.status = :orderStatus where o.id = "
                    + ":orderId")
    void updateStatus(@Param("orderId") Integer orderId,
            @Param("orderStatus") OrderStatus orderStatus);
}
