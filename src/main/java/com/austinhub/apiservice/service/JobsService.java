package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.CategoryType;
import com.austinhub.apiservice.model.PageList;
import com.austinhub.apiservice.model.dto.CreateJobDTO;
import com.austinhub.apiservice.model.dto.MyJobDTO;
import com.austinhub.apiservice.model.dto.OrderItemDTO;
import com.austinhub.apiservice.model.dto.UpdateJobRequest;
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

    public PageList<Job> findByCategory(int categoryId, int page, int pageSize, String query) {
        final int totalCount = jobRepository.countAll(categoryId, query);
        final List<Job> jobs = jobRepository.findAllByCategory(categoryId, page, pageSize, query);
        return PageList.<Job>builder()
                .page(page)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .entries(jobs)
                .build();
    }

    @Override
    public void save(OrderItemDTO orderItemDTO, Account account, Date createdTimestamp,
            Integer orderId, Integer membershipId, String resourceTypeName) {
        final CreateJobDTO createJobDTO = (CreateJobDTO) orderItemDTO;
        final ResourceType resourceType = resourceTypeRepository
                .findResourceTypeByTableName(resourceTypeName);
        final Resource resource = Resource.builder()
                .account(account)
                .createdTimestamp(createdTimestamp)
                .expirationTimestamp(
                        ApplicationUtils
                                .calculateOrderItemExpirationTimestamp(createJobDTO.getPricingPlan(),
                                        createdTimestamp))
                .orderId(orderId)
                .resourceType(resourceType)
                .build();
        final Category category = categoryRepository
                .findByNameAndCategoryType(createJobDTO.getCategoryName(),
                        CategoryType.RESC);
        final Job job = Job.builder()
                .resource(resource)
                .name(createJobDTO.getName())
                .description(createJobDTO.getDescription())
                .salary(createJobDTO.getSalary())
                .phone(createJobDTO.getPhone())
                .address(createJobDTO.getAddress())
                .contact(createJobDTO.getContact())
                .category(category)
                .companyLink(createJobDTO.getCompanyLink())
                .build();

        jobRepository.save(job);
    }

    public List<MyJobDTO> findOwnsJobs(String accountName, Boolean isArchived) {
        return jobRepository.findByAccountNameAndArchived(accountName, isArchived);
    }

    public void updateJob(UpdateJobRequest updates) {
        // Get existing ads
        Job job = jobRepository.getOne((updates.getId()));

        if (job == null) {
            throw new RuntimeException("Job does not exist");
        }

        Category newCategory =
                categoryRepository.findByNameAndCategoryType(updates.getCategoryName(),
                        CategoryType.RESC);

        if (newCategory == null) {
            throw new RuntimeException(
                    String.format("Category %s does not exist", updates.getCategoryName()));
        }

        job.setName(updates.getName());
        job.setDescription(updates.getDescription());
        job.setSalary(updates.getSalary());
        job.setPhone(updates.getPhone());
        job.setAddress(updates.getAddress());
        job.setContact(updates.getContact());
        job.setCategory(newCategory);
        job.setCompanyLink(updates.getCompanyLink());

        // Update ads
        jobRepository.save(job);
    }
}
