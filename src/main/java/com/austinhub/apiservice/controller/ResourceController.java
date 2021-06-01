package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.po.ResourceType;
import com.austinhub.apiservice.service.ResourceService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/resource")
public class ResourceController {
    private ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/types")
    public ResponseEntity<List<ResourceType>> findAllResourceTypes() {
        return ResponseEntity.ok().body(resourceService.findAllResourceTypes());
    }
}
