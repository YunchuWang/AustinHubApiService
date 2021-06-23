package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Booth;

import java.util.List;

public interface BoothRepositoryCustom {
    int countAll(final int categoryId, final String query);

    List<Booth> findAllByCategory(
            final int categoryId,
            final int page,
            final int pageSize,
            final String query
    );
}
