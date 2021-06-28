package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.PageList;
import com.austinhub.apiservice.model.dto.MyJobDTO;
import com.austinhub.apiservice.model.dto.UpdateJobRequest;
import com.austinhub.apiservice.model.enums.OrderBy;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.model.po.Job;
import com.austinhub.apiservice.service.CategoryService;
import com.austinhub.apiservice.service.JobsService;
import com.austinhub.apiservice.utils.GsonUtils;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping ("/owned")
    public ResponseEntity<List<MyJobDTO>> findOwnedJobs(@Valid @NotNull @RequestParam String accountName,
            @Valid @NotNull @RequestParam Boolean isArchived) {
        return ResponseEntity.ok().body(jobsService.findOwnsJobs(accountName, isArchived));
    }

    @PutMapping
    public ResponseEntity<String> updateJobs(@Valid @RequestBody UpdateJobRequest updates) {
        System.out.println(updates.toString());
        jobsService.updateJob(updates);
        return ResponseEntity.ok(GsonUtils.getGson().toJson("updated"));
    }

    @GetMapping
    public ResponseEntity<PageList<Job>> findJobsByCategory(
            @Valid @NotNull @RequestParam String name,
            @Valid @NotNull @RequestParam CategoryType type,
            @Valid @NotNull @RequestParam(defaultValue = "0") int page,
            @Valid @NotNull @RequestParam int pageSize,
            @Valid @NotNull @RequestParam(defaultValue = "") String query,
            @Valid @NotNull @RequestParam(defaultValue = "TITLE") OrderBy orderBy
    ) {
        Category category = categoryService.findCategory(name, type);
        final PageList<Job> jobs = jobsService.findByCategory(category.getId(), page, pageSize, query, orderBy);
        return ResponseEntity.ok().body(jobs);
    }

}
