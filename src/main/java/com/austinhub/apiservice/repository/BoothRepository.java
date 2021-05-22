package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.model.po.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Integer> {

    List<Booth> findAllByCategory(Category category);

    @Override
    <S extends Booth> S save(S s);

    @Override
    void delete(Booth booth);
}
