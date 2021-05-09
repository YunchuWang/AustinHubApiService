package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.BoothRequest;
import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.model.po.CategoryRelation;
import com.austinhub.apiservice.model.po.Resource;
import com.austinhub.apiservice.repository.BoothRepository;
import com.austinhub.apiservice.repository.ResourceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class BoothService {
  private BoothRepository boothRepository;
  private ResourceRepository resourceRepository;

  public List<Booth> findByCategory(int categoryRelationId) {
    CategoryRelation categoryRelation = CategoryRelation.builder().id(categoryRelationId).build();

    return boothRepository.findAllByCategoryRelation(categoryRelation);
  }

  public Booth saveBooth(BoothRequest boothRequest) {
    CategoryRelation categoryRelation = CategoryRelation.builder().id(boothRequest.getCategoryRelationId()).build();
    Resource resource = resourceRepository.findByName("booth");
    Booth booth =
        Booth.builder()
            .name(boothRequest.getName())
            .phone(boothRequest.getPhone())
            .email(boothRequest.getEmail())
            .description(boothRequest.getDescription())
                .resourceId(resource.getId())
            .categoryRelation(categoryRelation)
            .build();

    return boothRepository.save(booth);
  }
}
