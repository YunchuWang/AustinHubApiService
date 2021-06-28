package com.austinhub.apiservice.repositoryImpl;

import com.austinhub.apiservice.model.enums.OrderBy;
import com.austinhub.apiservice.model.po.Job;
import com.austinhub.apiservice.repository.JobRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

public class JobRepositoryCustomImpl implements JobRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int countAll(final int categoryId, final String queryStr) {
        final Query query = entityManager.createNativeQuery(
                String.format("select count(1) from job where categoryId=%1$s"
                        + " and ( name like '%2$s'"
                        + " or address like '%2$s'"
                        + " or description like '%2$s')", categoryId, "%" + queryStr + "%")
        );
        return ((BigInteger) query.getSingleResult()).intValue();
    }

    @Override
    public List<Job> findAllByCategory(
            final int categoryId,
            final int page,
            final int pageSize,
            final String queryStr,
            final OrderBy orderBy
    ) {
        final int offset = page * pageSize;
        final String orderByStr = getOrderByStr(orderBy);
        final Query query = entityManager.createNativeQuery(
                String.format(
                        "select * from job"
                                + " left join category on job.categoryId=category.id"
                                + " left join resource on job.resourceId=resource.id"
                                + " where job.categoryId=%1$s"
                                + " and (job.name like '%2$s' or job.address like '%2$s' or job.description like '%2$s')"
                                + " order by %3$s limit %4$s offset %5$s",
                        categoryId, "%" + queryStr + "%", orderByStr, pageSize, offset),
                Job.class
        );
        return query.getResultList();
    }

    private String getOrderByStr(final OrderBy orderBy) {
        switch (orderBy) {
            case TITLE:
                return "job.name ASC";
            case CREATED_TIMESTAMP:
                return "resource.createdTimestamp DESC";
            default:
                return "";
        }
    }
}
