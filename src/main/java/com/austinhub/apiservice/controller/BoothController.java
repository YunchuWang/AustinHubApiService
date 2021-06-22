package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.dto.CreateBoothRequest;
import com.austinhub.apiservice.model.dto.MyAdsDTO;
import com.austinhub.apiservice.model.dto.MyBoothDTO;
import com.austinhub.apiservice.model.dto.UpdateBoothRequest;
import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.service.BoothService;
import com.austinhub.apiservice.service.CategoryService;
import com.austinhub.apiservice.utils.GsonUtils;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/booths")
public class BoothController {

    private final BoothService boothService;
    private final CategoryService categoryService;

    public BoothController(BoothService boothService,
            CategoryService categoryService) {
        this.boothService = boothService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Booth>> findBoothsByCategory(
            @Valid @NotNull @RequestParam String name,
            @Valid @NotNull @RequestParam CategoryType type) {
        Category category = categoryService.findCategory(name, type);
        return ResponseEntity.ok().body(boothService.findByCategory(category.getId()));
    }

    @GetMapping ("/owned")
    public ResponseEntity<List<MyBoothDTO>> findOwnedBooths(@Valid @NotNull @RequestParam String accountName,
            @Valid @NotNull @RequestParam Boolean isArchived) {
        return ResponseEntity.ok().body(boothService.findOwnsBooths(accountName, isArchived));
    }
    
    @PostMapping
    public ResponseEntity<Booth> saveBooth(
            @Valid @RequestBody CreateBoothRequest createBoothRequest) {
        return ResponseEntity.ok(boothService.saveBooth(createBoothRequest));
    }

    @PutMapping
    public ResponseEntity<String> updateBooth(@Valid @RequestBody UpdateBoothRequest updates) {
        System.out.println(updates.toString());
        boothService.updateBooth(updates);
        return ResponseEntity.ok(GsonUtils.getGson().toJson("updated"));
    }
}
