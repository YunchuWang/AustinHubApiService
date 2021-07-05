package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.dto.RenewableItemDTO;
import com.austinhub.apiservice.model.po.Resource;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    @Modifying
    @Query(value =
            "update Resource res set res.isArchived = :isArchived where res.id = "
                    + ":resourceId")
    void updateIsArchived(@Param("resourceId") Integer resourceId,
            @Param("isArchived") Boolean isArchived);

    @Query(value =
            "select CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END from resource r "
                    + "inner join resource_order ro on r.id = ro.resourceId "
                    + "inner join `order` o on o.id = ro.orderId "
                    + "where r.accountId = :accountId and CAST(expirationTimestamp "
                    + "as date) = CAST(:expirationTime as date) and o.status = 'COMPLETED'",
            nativeQuery = true)
    boolean existsUnexpiredResource(@Param("accountId") Integer accountId, @Param(
            "expirationTime") Date expirationTime);

    @Query(value = "(select r.id as id, r.expirationTimestamp as expirationTime, r.name as name, "
            + "resource_type.tableName as type, r.categoryName as category from resource r "
            + "inner join resource_type on r.resourceTypeId = resource_type.id "
            + "inner join resource_order on r.id = resource_order.resourceId "
            + "inner join `order` o on resource_order.orderId = o.id "
            + "where o.status = 'COMPLETED' and r.accountId = "
            + ":accountId and r.isArchived = false and DATE(r.expirationTimestamp) >= DATE(NOW()) "
            + "group by r.id)"
            + "union "
            + "(select m.id as id, m.expirationTimestamp as expirationTime, membership_type.name "
            + "as name, 'membership' as type, '' as category from membership m "
            + "inner join membership_type on m.membershipTypeId = membership_type.id "
            + "inner join membership_order on membership_order.membershipId = m.id "
            + "inner join `order` o on membership_order.orderId = o.id "
            + "where o.status = 'COMPLETED' and m.accountId = :accountId and DATE(m"
            + ".expirationTimestamp) >= DATE(NOW()) "
            + "group by m.id)",
            nativeQuery = true)
    List<RenewableItemDTO> getRenewableItems(@Param("accountId") Integer accountId);
}
