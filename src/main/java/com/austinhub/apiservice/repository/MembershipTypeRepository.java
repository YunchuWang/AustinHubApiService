package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.dto.ResourceLimit;
import com.austinhub.apiservice.model.po.MembershipType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MembershipTypeRepository extends JpaRepository<MembershipType, Integer> {

    MembershipType findMembershipTypeByName(String name);
    @Override
    List<MembershipType> findAll();

    @Query(nativeQuery = true, value =
            "SELECT membership_type.name as membershipName, resource_type.tableName as resourceName, quantity\n"
                    + " from membership_type inner join membership_resource_limit\n"
                    + "on membership_type.id = membership_resource_limit.membershipTypeId\n"
                    + "inner join resource_type\n"
                    + "on membership_resource_limit.resourceTypeId = resource_type.id;")
    List<ResourceLimit> findResourceLimits();
}
