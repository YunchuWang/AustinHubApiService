package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer>, JobRepositoryCustom {
}
