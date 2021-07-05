package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.RenewableItemDTO;
import com.austinhub.apiservice.model.dto.UpdateResourceArchivedDTO;
import com.austinhub.apiservice.model.po.ResourceType;
import com.austinhub.apiservice.service.ResourceService;
import com.austinhub.apiservice.utils.GsonUtils;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/types")
    public ResponseEntity<List<ResourceType>> findAllResourceTypes() {
        return ResponseEntity.ok().body(resourceService.findAllResourceTypes());
    }

    @PutMapping
    public ResponseEntity<String> updateArchived(
            @Valid @RequestBody UpdateResourceArchivedDTO updateResourceArchivedDTO) {
        resourceService.updateIsArchived(updateResourceArchivedDTO.getResourceId(),
                updateResourceArchivedDTO.getIsArchived());
        return ResponseEntity.ok(GsonUtils.getGson().toJson("updated"));
    }

    @GetMapping("/{username}/renewable")
    public ResponseEntity<List<RenewableItemDTO>> findRenewable(@PathVariable(required = true) String username) {
        return ResponseEntity.ok().body(resourceService.findRenewableItems(username));
    }
}
