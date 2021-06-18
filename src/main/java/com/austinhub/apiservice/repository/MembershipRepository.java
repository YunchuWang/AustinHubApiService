package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {

}
