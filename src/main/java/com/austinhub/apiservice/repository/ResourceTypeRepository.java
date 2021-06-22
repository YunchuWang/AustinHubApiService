package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.ResourceType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, Integer> {

    @Override
    List<ResourceType> findAll();

    ResourceType findResourceTypeByTableName(String tableName);
}