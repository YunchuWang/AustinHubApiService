package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Resource;
import java.util.Date;
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
    void updateIsArchived( @Param("resourceId") Integer resourceId,
            @Param("isArchived")  Boolean isArchived);

    @Query(value =
            "select CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END from resource "
                    + "where accountId = :accountId and CAST(expirationTimestamp "
                    + "as date) = CAST(:expirationTime as date)",
            nativeQuery = true)
    boolean existsUnexpiredResource(@Param("accountId") Integer accountId, @Param(
            "expirationTime") Date expirationTime);
}
