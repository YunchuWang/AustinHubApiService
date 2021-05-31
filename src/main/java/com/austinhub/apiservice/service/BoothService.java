package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.BoothRequest;
import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.repository.BoothRepository;
import com.austinhub.apiservice.repository.ResourceTypeRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class BoothService {
  private BoothRepository boothRepository;
  private ResourceTypeRepository resourceRepository;

  public List<Booth> findByCategory(int categoryId) {
    Category category = Category.builder().id(categoryId).build();

    return boothRepository.findAllByCategory(category);
  }

  public Booth saveBooth(BoothRequest boothRequest) {
//    Resource resource = resourceRepository.findByName("booth");
//    Booth booth =
//        Booth.builder()
//            .name(boothRequest.getName())
//            .phone(boothRequest.getPhone())
//            .email(boothRequest.getEmail())
//            .description(boothRequest.getDescription())
//                .resourceId(resource.getId())
//            .category(Category.builder().id(boothRequest.getCategoryRelationId()).build())
//            .build();
    return null;
//    return boothRepository.save(booth);
  }
}
