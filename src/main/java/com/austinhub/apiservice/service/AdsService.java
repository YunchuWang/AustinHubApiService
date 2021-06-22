package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.dto.CreateAdsDTO;
import com.austinhub.apiservice.model.dto.MyAdsDTO;
import com.austinhub.apiservice.model.dto.OrderItemDTO;
import com.austinhub.apiservice.model.dto.UpdateAdsRequest;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Ads;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.model.po.Resource;
import com.austinhub.apiservice.model.po.ResourceType;
import com.austinhub.apiservice.repository.AdsRepository;
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
public class AdsService implements IOrderItemSaveService {

    private AdsRepository adsRepository;
    private CategoryRepository categoryRepository;
    private ResourceTypeRepository resourceTypeRepository;

    public List<Ads> findAllAds() {
        return adsRepository.findAll();
    }

    public List<MyAdsDTO> findOwnsAds(String accountName, Boolean isArchived) {
        return adsRepository.findByAccountNameAndArchived(accountName, isArchived);
    }

    public void saveAds(List<Ads> ads) {
        adsRepository.saveAll(ads);
    }

    public void updateAds(UpdateAdsRequest updates) {
        // Get existing ads
        Ads ads = adsRepository.getOne((updates.getId()));
        if (ads == null) {
            throw new RuntimeException("Ads does not exist");
        }

        Category newCategory =
                categoryRepository.findByNameAndCategoryType(updates.getCategoryName(),
                        CategoryType.RESC);

        if (newCategory == null) {
            throw new RuntimeException(
                    String.format("Category %s does not exist", updates.getCategoryName()));
        }
        
        ads.setName(updates.getName());
        ads.setAddress(updates.getAddress());
        ads.setPhone(updates.getPhone());
        ads.setEmail(updates.getEmail());
        ads.setDescription(updates.getDescription());
        ads.setCategory(newCategory);
        ads.setWebLink(updates.getWebLink());
        ads.setImageLink(updates.getImageLink());
        
        // Update ads
        adsRepository.save(ads);
    }

    @Override
    public void save(OrderItemDTO orderItemDTO, Account account,
            Date createdTimestamp, Integer orderId, Integer membershipId, String resourceTypeName) {
        final CreateAdsDTO createAdsDTO = (CreateAdsDTO) orderItemDTO;
        final ResourceType resourceType = resourceTypeRepository
                .findResourceTypeByTableName(resourceTypeName);
        final Resource resource = Resource.builder()
                .account(account)
                .createdTimestamp(createdTimestamp)
                .expirationTimestamp(
                        ApplicationUtils
                                .calculateOrderItemExpirationTimestamp(
                                        createAdsDTO.getPricingPlan(),
                                        createdTimestamp))
                .orderId(orderId)
                .resourceType(resourceType)
                .build();
        final Category category = categoryRepository
                .findByNameAndCategoryType(createAdsDTO.getCategoryName(),
                        CategoryType.RESC);
        final Ads ads = Ads.builder()
                .resource(resource)
                .name(createAdsDTO.getName())
                .phone(createAdsDTO.getPhone())
                .email(createAdsDTO.getEmail())
                .description(createAdsDTO.getDescription())
                .category(category)
                .webLink(createAdsDTO.getWebLink())
                .imageLink(createAdsDTO.getImageUploaded())
                .address(createAdsDTO.getAddress())
                .build();

        adsRepository.save(ads);
    }
}
