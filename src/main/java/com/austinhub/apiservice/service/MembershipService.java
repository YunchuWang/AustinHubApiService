package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.MembershipTypeDto;
import com.austinhub.apiservice.model.dto.ResourceLimit;
import com.austinhub.apiservice.model.po.MembershipType;
import com.austinhub.apiservice.repository.MembershipTypeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MembershipService {

    private MembershipTypeRepository membershipTypeRepository;

    public List<MembershipTypeDto> findAll() {
        List<ResourceLimit> resourceLimits = membershipTypeRepository.findResourceLimits();
        List<MembershipType> membershipTypes = membershipTypeRepository.findAll();

        return membershipTypes
                .stream()
                .map(membershipType -> membershipType.toDto(resourceLimits.stream()
                        .filter(resourceLimit -> resourceLimit.getMembershipName()
                                .equals(membershipType.getName())).collect(
                                Collectors.toList())))
                .collect(Collectors.toList());
    }
}
