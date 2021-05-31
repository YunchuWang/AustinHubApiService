package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.ResourceType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, Integer> {

    @Override
    List<ResourceType> findAll();
}
