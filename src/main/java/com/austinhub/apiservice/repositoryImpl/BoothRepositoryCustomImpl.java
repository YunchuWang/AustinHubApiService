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
    private static final Integer ALL_CATEGORY_ID = 12;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int countAll(final int categoryId, final String queryStr) {
        final String categoryIdStr = ALL_CATEGORY_ID.equals(categoryId)
                ? ""
                : "categoryId=" + categoryId + " and";
        final Query query = entityManager.createNativeQuery(
                String.format(
                        "select count(distinct booth.id) from booth"
                                + " left join category on booth.categoryId=category.id"
                                + " left join resource on booth.resourceId=resource.id"
                                + " left join resource_order on booth.resourceId=resource_order.resourceId"
                                + " left join `order` on resource_order.orderId=`order`.id"
                                + " where %1$s"
                                + " (booth.name like '%2$s' or booth.address like '%2$s' or booth.description like '%2$s')"
                                + " and (resource.expirationTimestamp >= CURRENT_TIMESTAMP)"
                                + " and resource.isArchived=0"
                                + " and `order`.status='COMPLETED'",
                        categoryIdStr, "%" + queryStr + "%")
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
        final String categoryIdStr = ALL_CATEGORY_ID.equals(categoryId)
                ? ""
                : "booth.categoryId=" + categoryId + " and";
        final String orderByStr = getOrderByStr(orderBy);
        final Query query = entityManager.createNativeQuery(
                String.format(
                        "select booth.*, category.*, resource.*, MAX(`order`.createdTimestamp) from booth"
                                + " left join category on booth.categoryId=category.id"
                                + " left join resource on booth.resourceId=resource.id"
                                + " left join resource_order on booth.resourceId=resource_order.resourceId"
                                + " left join `order` on resource_order.orderId=`order`.id"
                                + " where %1$s"
                                + " (booth.name like '%2$s' or booth.address like '%2$s' or booth.description like '%2$s')"
                                + " and resource.expirationTimestamp >= CURRENT_TIMESTAMP"
                                + " and resource.isArchived=0"
                                + " and `order`.status='COMPLETED'"
                                + " group by booth.id"
                                + " order by %3$s limit %4$s offset %5$s",
                        categoryIdStr, "%" + queryStr + "%", orderByStr, pageSize, offset),
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
