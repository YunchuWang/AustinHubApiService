package com.austinhub.apiservice.repositoryImpl;

import com.austinhub.apiservice.model.enums.OrderBy;
import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.repository.BoothRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

public class BoothRepositoryCustomImpl implements BoothRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int countAll(final int categoryId, final String queryStr) {
        final Query query = entityManager.createNativeQuery(
                String.format("select count(1) from booth where categoryId=%1$s"
                        + " and ( name like '%2$s'"
                        + " or address like '%2$s'"
                        + " or description like '%2$s')", categoryId, "%" + queryStr + "%")
        );
        return ((BigInteger) query.getSingleResult()).intValue();
    }

    @Override
    public List<Booth> findAllByCategory(
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
                        "select * from booth"
                                + " left join category on booth.categoryId=category.id"
                                + " left join resource on booth.resourceId=resource.id"
                                + " where booth.categoryId=%1$s"
                                + " and (booth.name like '%2$s' or booth.address like '%2$s' or booth.description like '%2$s')"
                                + " order by %3$s limit %4$s offset %5$s",
                        categoryId, "%" + queryStr + "%", orderByStr, pageSize, offset),
                Booth.class
        );
        return query.getResultList();
    }


    private String getOrderByStr(final OrderBy orderBy) {
        switch (orderBy) {
            case TITLE:
                return "booth.name ASC";
            case CREATED_TIMESTAMP:
                return "resource.createdTimestamp DESC";
            default:
                return "";
        }
    }
}
