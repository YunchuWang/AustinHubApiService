package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.po.Category;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Integer> {
    public List<Category> findByCategoryType(CategoryType categoryType);
}
