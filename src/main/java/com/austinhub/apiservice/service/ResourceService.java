package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.po.ResourceType;
import com.austinhub.apiservice.repository.ResourceTypeRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class ResourceService {
    private ResourceTypeRepository resourceTypeRepository;

    public List<ResourceType> findAllResourceTypes() {
        return resourceTypeRepository.findAll();
    }
}
