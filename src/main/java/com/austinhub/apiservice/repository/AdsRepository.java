package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {

}
