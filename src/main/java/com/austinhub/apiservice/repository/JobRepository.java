package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Category;
import com.austinhub.apiservice.model.po.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByCategory(Category category);
}
