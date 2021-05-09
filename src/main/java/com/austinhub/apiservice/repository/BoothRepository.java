package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.model.po.CategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Integer> {
  List<Booth> findAllByCategoryRelation(CategoryRelation categoryRelation);

  @Override
  <S extends Booth> S save(S s);

  @Override
  void delete(Booth booth);
}
