package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.RenewOrderItemDTO;
import com.austinhub.apiservice.model.dto.RenewableItemDTO;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Order;
import com.austinhub.apiservice.model.po.Resource;
import com.austinhub.apiservice.model.po.ResourceType;
import com.austinhub.apiservice.repository.AccountRepository;
import com.austinhub.apiservice.repository.ResourceRepository;
import com.austinhub.apiservice.repository.ResourceTypeRepository;
import com.austinhub.apiservice.utils.ApplicationUtils;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class ResourceService {

    private ResourceTypeRepository resourceTypeRepository;
    private ResourceRepository resourceRepository;
    private AccountRepository accountRepository;

    public List<ResourceType> findAllResourceTypes() {
        return resourceTypeRepository.findAll();
    }

    public void updateIsArchived(Integer resourceId, Boolean isArchived) {
        resourceRepository.updateIsArchived(resourceId, isArchived);
    }

    public List<RenewableItemDTO> findRenewableItems(@NonNull final String username) {
        final Account account = accountRepository.findByUsername(username);
        return resourceRepository.getRenewableItems(account.getId());
    }

    public void renew(RenewOrderItemDTO renewOrderItemDTO, Order order) {
        final Resource resource = resourceRepository.getOne(renewOrderItemDTO.getItemId());
        if (resource == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              String.format("No resource found with id %d",
                                                            renewOrderItemDTO.getItemId()));
        }

        resource.getOrders().add(order);
        resourceRepository.save(resource);
    }

    public void extendExpiration(RenewOrderItemDTO renewOrderItemDTO) {
        final Resource resource = resourceRepository.getOne(renewOrderItemDTO.getItemId());
        Date expirationTimestamp = resource.getExpirationTimestamp().before(new Date()) ?
                new Date() : resource.getExpirationTimestamp();
        resource.setExpirationTimestamp(ApplicationUtils
                                                .calculateOrderItemExpirationTimestamp(
                                                        renewOrderItemDTO.getPricingPlan(),
                                                        expirationTimestamp));

        resourceRepository.save(resource);
    }
}
