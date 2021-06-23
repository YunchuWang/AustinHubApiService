package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.dto.MyJobDTO;
import com.austinhub.apiservice.model.po.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer>, JobRepositoryCustom {

    @Query(value = "select job.*, category.name as categoryName, resource.expirationTimestamp "
            + "as "
            + "expirationTime from job "
            + "inner "
            + "join "
            + "resource "
            + "on job.resourceId = resource.id "
            + "inner join account "
            + "on resource.accountId = account.id "
            + "inner join category "
            + "on job.categoryId = category.id "
            + "where account.username = :accountName and resource.isArchived = :isArchived",
            nativeQuery = true)
    List<MyJobDTO> findByAccountNameAndArchived(@Param("accountName") String accountName, @Param(
            "isArchived") Boolean isArchived);
}
