package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {

    @Modifying
    @Query(value =
            "update Membership mem set mem.autoSubscribed = :autoSubscribed where mem.id = "
                    + ":membershipId")
    void updateAutoSubscribed(@Param("membershipId") Integer membershipId,
            @Param("autoSubscribed") Boolean autoSubscribed);
}
