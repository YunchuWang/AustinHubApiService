package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.PageList;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.model.po.Job;
import com.austinhub.apiservice.service.CategoryService;
import com.austinhub.apiservice.service.JobsService;
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
    public ResponseEntity<PageList<Job>> findJobsByCategory(
            @Valid @NotNull @RequestParam String name,
            @Valid @NotNull @RequestParam CategoryType type,
            @Valid @NotNull @RequestParam int page,
            @Valid @NotNull @RequestParam int pageSize,
            @Valid @NotNull @RequestParam String query
    ) {
        Category category = categoryService.findCategory(name, type);
        final PageList<Job> jobs = jobsService.findByCategory(category.getId(), page, pageSize, query);
        return ResponseEntity.ok().body(jobs);
    }

}
