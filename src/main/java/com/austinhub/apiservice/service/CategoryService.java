package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.repository.CategoryRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService {

    @PersistenceContext
    private EntityManager entityManager;
    private CategoryRepository categoryRepository;

//  @Transactional
//  public List<CategoryRelationDto> findAllCategoryRelations() {
//    QCategory qCategory = QCategory.category;
//    QSubcategory qSubcategory = QSubcategory.subcategory;
//    QCategoryRelation qCategoryRelation = QCategoryRelation.categoryRelation;
//
//    return new JPAQuery<CategoryRelationDto>(entityManager)
//        .select(
//            Projections.bean(
//                CategoryRelationDto.class,
//                qCategoryRelation.id.as("id"),
//                qCategory.id.as("categoryId"),
//                qCategory.name.as("categoryName"),
//                qSubcategory.id.as("subCategoryId"),
//                qSubcategory.name.as("subCategoryName")))
//        .from(qCategory)
//        .innerJoin(qCategoryRelation)
//        .on(qCategory.id.eq(qCategoryRelation.categoryId))
//        .innerJoin(qSubcategory)
//        .on(qCategoryRelation.subcategoryId.eq(qSubcategory.id)).fetch();
//  }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
}
