package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Integer> {
//  @Query("nativeQuery = true)
//  List<CategoryRelationDto> findAll();
}
