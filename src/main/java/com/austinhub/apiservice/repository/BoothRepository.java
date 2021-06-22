package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.dto.MyBoothDTO;
import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.model.po.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Integer> {

    List<Booth> findAllByCategory(Category category);

    @Override
    <S extends Booth> S save(S s);

    @Override
    void delete(Booth booth);
    
    @Query(value = "select booth.*, category.name as categoryName, resource.expirationTimestamp "
            + "as "
            + "expirationTime from booth "
            + "inner "
            + "join "
            + "resource "
            + "on booth.resourceId = resource.id "
            + "inner join account "
            + "on resource.accountId = account.id "
            + "inner join category "
            + "on booth.categoryId = category.id "
            + "where account.username = :accountName and resource.isArchived = :isArchived",
            nativeQuery = true)
    List<MyBoothDTO> findByAccountNameAndArchived(@Param("accountName") String accountName, @Param(
            "isArchived") Boolean isArchived);
}
