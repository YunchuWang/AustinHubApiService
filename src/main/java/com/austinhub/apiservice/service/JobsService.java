package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.model.po.Job;
import com.austinhub.apiservice.repository.JobRepository;
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
public class JobsService {
    private JobRepository jobRepository;

    public List<Job> findByCategory(int categoryId) {
        Category category = Category.builder().id(categoryId).build();

        return jobRepository.findByCategory(category);
    }
}
