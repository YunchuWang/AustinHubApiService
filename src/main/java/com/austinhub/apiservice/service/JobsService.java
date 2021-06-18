package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.dto.JobDTO;
import com.austinhub.apiservice.model.dto.OrderItemDTO;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.model.po.Job;
import com.austinhub.apiservice.model.po.Resource;
import com.austinhub.apiservice.model.po.ResourceType;
import com.austinhub.apiservice.repository.CategoryRepository;
import com.austinhub.apiservice.repository.JobRepository;
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
public class JobsService implements IOrderItemSaveService {

    private JobRepository jobRepository;
    private CategoryRepository categoryRepository;
    private ResourceTypeRepository resourceTypeRepository;

    public List<Job> findByCategory(int categoryId) {
        Category category = Category.builder().id(categoryId).build();

        return jobRepository.findByCategory(category);
    }

    @Override
    public void save(OrderItemDTO orderItemDTO, Account account, Date createdTimestamp,
            Integer orderId, Integer membershipId, String resourceTypeName) {
        final JobDTO jobDTO = (JobDTO) orderItemDTO;
        final ResourceType resourceType = resourceTypeRepository
                .findResourceTypeByTableName(resourceTypeName);
        final Resource resource = Resource.builder()
                .account(account)
                .createdTimestamp(createdTimestamp)
                .expirationTimestamp(
                        ApplicationUtils
                                .calculateOrderItemExpirationTimestamp(jobDTO.getPricingPlan(),
                                        createdTimestamp))
                .orderId(orderId)
                .resourceType(resourceType)
                .build();
        final Category category = categoryRepository
                .findByNameAndCategoryType(jobDTO.getCategoryName(),
                        CategoryType.RESC);
        final Job job = Job.builder()
                .resource(resource)
                .name(jobDTO.getName())
                .description(jobDTO.getDescription())
                .salary(jobDTO.getSalary())
                .phone(jobDTO.getPhone())
                .address(jobDTO.getAddress())
                .contact(jobDTO.getContact())
                .category(category)
                .companyLink(jobDTO.getCompanyLink())
                .build();

        jobRepository.save(job);
    }
}
