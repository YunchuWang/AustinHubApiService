package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.model.po.Job;
import com.austinhub.apiservice.service.CategoryService;
import com.austinhub.apiservice.service.JobsService;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/jobs")
public class JobController {
    private JobsService jobsService;
    private CategoryService categoryService;

    public JobController(JobsService jobsService,
            CategoryService categoryService) {
        this.jobsService = jobsService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Job>> findJobsByCategory(
            @Valid @NotNull @RequestParam String name, @Valid @NotNull @RequestParam CategoryType type) {
        Category category = categoryService.findCategory(name, type);
        Objects.requireNonNull(category);
        return ResponseEntity.ok().body(jobsService.findByCategory(category.getId()));
    }
}
