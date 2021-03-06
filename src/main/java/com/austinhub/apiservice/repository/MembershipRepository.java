package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Membership;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {

    @Query(value =
            "select CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END from membership "
                    + "mem "
                    + "inner join membership_order mo on mem.id = mo.membershipId "
                    + "inner join `order` o on o.id = mo.orderId "
                    + "where mem.accountId = :accountId and CAST"
                    + "(mem.expirationTimestamp "
                    + "as date) = CAST(:expirationTime as date) "
                    + "and o.status = 'COMPLETED'",
            nativeQuery = true)
    boolean existsUnexpiredMembership(@Param("accountId") Integer accountId, @Param(
            "expirationTime") Date expirationTime);
}
