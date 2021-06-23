package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.PageList;
import com.austinhub.apiservice.model.dto.CreateBoothDTO;
import com.austinhub.apiservice.model.dto.CreateBoothRequest;
import com.austinhub.apiservice.model.dto.MyBoothDTO;
import com.austinhub.apiservice.model.dto.OrderItemDTO;
import com.austinhub.apiservice.model.dto.UpdateBoothRequest;
import com.austinhub.apiservice.model.po.Account;
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

    public PageList<Booth> findByCategory(int categoryId, int page, int pageSize, String query) {
        final int totalCount = boothRepository.countAll(categoryId, query);
        final List<Booth> booths = boothRepository.findAllByCategory(categoryId, page, pageSize, query);
        return PageList.<Booth>builder()
                .page(page)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .entries(booths)
                .build();
    }

    public Booth saveBooth(CreateBoothRequest createBoothRequest) {
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
        final CreateBoothDTO createBoothDTO = (CreateBoothDTO) orderItemDTO;
        final ResourceType resourceType = resourceTypeRepository
                .findResourceTypeByTableName(resourceTypeName);
        final Resource resource = Resource.builder()
                .account(account)
                .createdTimestamp(createdTimestamp)
                .expirationTimestamp(
                        ApplicationUtils
                                .calculateOrderItemExpirationTimestamp(
                                        createBoothDTO.getPricingPlan(),
                                        createdTimestamp))
                .orderId(orderId)
                .resourceType(resourceType)
                .build();
        final Category category = categoryRepository
                .findByNameAndCategoryType(createBoothDTO.getCategoryName(),
                        CategoryType.RESC);
        System.out.println(category.toString());
        final Booth booth = Booth.builder()
                .resource(resource)
                .name(createBoothDTO.getName())
                .phone(createBoothDTO.getPhone())
                .email(createBoothDTO.getEmail())
                .description(createBoothDTO.getDescription())
                .category(category)
                .webLink(createBoothDTO.getWebLink())
                .address(createBoothDTO.getAddress())
                .build();

        boothRepository.save(booth);
    }

    public void updateBooth(UpdateBoothRequest updates) {
        // Get existing booth
        Booth booth = boothRepository.getOne(updates.getId());

        if (booth == null) {
            throw new RuntimeException("Booth does not exist");
        }

        Category newCategory =
                categoryRepository.findByNameAndCategoryType(updates.getCategoryName(),
                        CategoryType.RESC);

        if (newCategory == null) {
            throw new RuntimeException(
                    String.format("Category %s does not exist", updates.getCategoryName()));
        }

        booth.setName(updates.getName());
        booth.setAddress(updates.getAddress());
        booth.setPhone(updates.getPhone());
        booth.setEmail(updates.getEmail());
        booth.setDescription(updates.getDescription());
        booth.setWebLink(updates.getWebLink());
        booth.setCategory(newCategory);
        // Update booth
        boothRepository.save(booth);
    }

    public List<MyBoothDTO> findOwnsBooths(String accountName, Boolean isArchived) {
        return boothRepository.findByAccountNameAndArchived(accountName, isArchived);
    }
}
