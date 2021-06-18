package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.dto.AdsDTO;
import com.austinhub.apiservice.model.dto.OrderItemDTO;
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

    public void saveAds(List<Ads> ads) {
        adsRepository.saveAll(ads);
    }

    @Override
    public void save(OrderItemDTO orderItemDTO, Account account,
            Date createdTimestamp, Integer orderId, Integer membershipId, String resourceTypeName) {
        final AdsDTO adsDTO = (AdsDTO) orderItemDTO;
        final ResourceType resourceType = resourceTypeRepository.findResourceTypeByTableName(resourceTypeName);
        final Resource resource = Resource.builder()
                .account(account)
                .createdTimestamp(createdTimestamp)
                .expirationTimestamp(
                        ApplicationUtils
                                .calculateOrderItemExpirationTimestamp(adsDTO.getPricingPlan(),
                                        createdTimestamp))
                .orderId(orderId)
                .resourceType(resourceType)
                .build();
        final Category category = categoryRepository
                .findByNameAndCategoryType(adsDTO.getCategoryName(),
                        CategoryType.RESC);
        System.out.println(category.toString());
        final Ads ads = Ads.builder()
                .resource(resource)
                .name(adsDTO.getName())
                .phone(adsDTO.getPhone())
                .email(adsDTO.getEmail())
                .description(adsDTO.getDescription())
                .category(category)
                .webLink(adsDTO.getWebLink())
                .imageLink(adsDTO.getImageUploaded())
                .address(adsDTO.getAddress())
                .build();

        adsRepository.save(ads);
    }
}
