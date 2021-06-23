package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.PageList;
import com.austinhub.apiservice.model.dto.BoothRequest;
import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.service.BoothService;
import com.austinhub.apiservice.service.CategoryService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/booths")
public class BoothController {

  private BoothService boothService;
  private CategoryService categoryService;

  public BoothController(BoothService boothService,
          CategoryService categoryService) {
    this.boothService = boothService;
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<PageList<Booth>> findBoothsByCategory(
      @Valid @NotNull @RequestParam String name,
      @Valid @NotNull @RequestParam CategoryType type,
      @Valid @NotNull @RequestParam int page,
      @Valid @NotNull @RequestParam int pageSize,
      @Valid @NotNull @RequestParam String query
  ) {
    Category category = categoryService.findCategory(name, type);
    final PageList<Booth> booths = boothService.findByCategory(category.getId(), page, pageSize, query);
    return ResponseEntity.ok().body(booths);
  }

  @PostMapping
  public ResponseEntity<Booth> saveBooth(@Valid @RequestBody BoothRequest boothRequest) {
    return ResponseEntity.ok(boothService.saveBooth(boothRequest));
  }
}
