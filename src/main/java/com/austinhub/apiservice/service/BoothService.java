package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.dto.AdsDTO;
import com.austinhub.apiservice.model.dto.BoothDTO;
import com.austinhub.apiservice.model.dto.BoothRequest;
import com.austinhub.apiservice.model.dto.OrderItemDTO;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Ads;
import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.model.po.Resource;
import com.austinhub.apiservice.model.po.ResourceType;
import com.austinhub.apiservice.repository.BoothRepository;
import com.austinhub.apiservice.repository.CategoryRepository;
import com.austinhub.apiservice.repository.ResourceTypeRepository;
import com.austinhub.apiservice.utils.ApplicationUtils;
import java.util.Date;
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
public class BoothService implements IOrderItemSaveService {

    private BoothRepository boothRepository;
    private CategoryRepository categoryRepository;
    private ResourceTypeRepository resourceTypeRepository;

    public List<Booth> findByCategory(int categoryId) {
        Category category = Category.builder().id(categoryId).build();

        return boothRepository.findAllByCategory(category);
    }

    public Booth saveBooth(BoothRequest boothRequest) {
//    Booth booth =
//        Booth.builder()
//            .name(boothRequest.getName())
//            .phone(boothRequest.getPhone())
//            .email(boothRequest.getEmail())
//            .description(boothRequest.getDescription())
//                .resourceId(resource.getId())
//            .category(Category.builder().id(boothRequest.getCategoryRelationId()).build())
//            .build();
//    return boothRepository.save(booth);
        return null;
    }

    @Override
    public void save(OrderItemDTO orderItemDTO, Account account, Date createdTimestamp,
            Integer orderId, Integer membershipId, String resourceTypeName) {
        final BoothDTO boothDTO = (BoothDTO) orderItemDTO;
        final ResourceType resourceType = resourceTypeRepository
                .findResourceTypeByTableName(resourceTypeName);
        final Resource resource = Resource.builder()
                .account(account)
                .createdTimestamp(createdTimestamp)
                .expirationTimestamp(
                        ApplicationUtils
                                .calculateOrderItemExpirationTimestamp(boothDTO.getPricingPlan(),
                                        createdTimestamp))
                .orderId(orderId)
                .resourceType(resourceType)
                .build();
        final Category category = categoryRepository
                .findByNameAndCategoryType(boothDTO.getCategoryName(),
                        CategoryType.RESC);
        System.out.println(category.toString());
        final Booth booth = Booth.builder()
                .resource(resource)
                .name(boothDTO.getName())
                .phone(boothDTO.getPhone())
                .email(boothDTO.getEmail())
                .description(boothDTO.getDescription())
                .category(category)
                .webLink(boothDTO.getWebLink())
                .address(boothDTO.getAddress())
                .build();

        boothRepository.save(booth);
    }
}
