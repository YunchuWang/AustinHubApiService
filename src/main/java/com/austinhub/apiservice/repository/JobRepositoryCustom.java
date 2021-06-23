package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Job;

import java.util.List;

public interface JobRepositoryCustom {
    int countAll(final int categoryId, final String query);

    List<Job> findAllByCategory(
            final int categoryId,
            final int page,
            final int pageSize,
            final String query
    );
}
