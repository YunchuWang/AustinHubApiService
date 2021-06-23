package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.dto.MyAdsDTO;
import com.austinhub.apiservice.model.po.Ads;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {

    @Query(value = "select ads.*, category.name as categoryName, resource.expirationTimestamp as "
            + "expirationTime from ads "
            + "inner "
            + "join "
            + "resource "
            + "on ads.resourceId = resource.id "
            + "inner join account "
            + "on resource.accountId = account.id "
            + "inner join category "
            + "on ads.categoryId = category.id "
            + "where account.username = :accountName and resource.isArchived = :isArchived",
            nativeQuery = true)
    List<MyAdsDTO> findByAccountNameAndArchived(@Param("accountName") String accountName, @Param(
            "isArchived") Boolean isArchived);
}
