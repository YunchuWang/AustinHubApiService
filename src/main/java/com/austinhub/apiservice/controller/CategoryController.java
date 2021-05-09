package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/categories")
public class CategoryController {

  private CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  //    @GetMapping
  //    public ResponseEntity<List<CategoryRelationDto>> findAllCategoryRelations() {
  //        return ResponseEntity.ok().body(categoryService.findAllCategoryRelations());
  //    }
  @GetMapping
  public ResponseEntity<List<Category>> findAllCategories() {
    return ResponseEntity.ok().body(categoryService.findAllCategories());
  }
}
