package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.CreateAdsDTO;
import com.austinhub.apiservice.model.dto.CreateBoothDTO;
import com.austinhub.apiservice.model.dto.CreateJobDTO;
import com.austinhub.apiservice.model.dto.CreateMembershipDTO;
import com.austinhub.apiservice.model.dto.MembershipTypeDTO;
import com.austinhub.apiservice.model.dto.OrderItemDTO;
import com.austinhub.apiservice.model.dto.ResourceLimit;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Membership;
import com.austinhub.apiservice.model.po.MembershipType;
import com.austinhub.apiservice.repository.MembershipRepository;
import com.austinhub.apiservice.repository.MembershipTypeRepository;
import com.austinhub.apiservice.utils.ApplicationUtils;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MembershipService implements IOrderItemSaveService {

    private MembershipRepository membershipRepository;
    private MembershipTypeRepository membershipTypeRepository;
    private AdsService adsService;
    private JobsService jobsService;
    private BoothService boothService;

    public List<MembershipTypeDTO> findAll() {
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

    @Override
    public void save(OrderItemDTO orderItemDTO, Account account,
            Date createdTimestamp, Integer orderId, Integer membershipId,
            String resourceTypeName) {
        CreateMembershipDTO createMembershipDTO = (CreateMembershipDTO) orderItemDTO;
        MembershipType membershipType = membershipTypeRepository
                .findMembershipTypeByName(createMembershipDTO.getMembershipType().getType());
        Membership savedMembership = membershipRepository
                .save(Membership.builder()
                        .membershipType(membershipType)
                        .account(account)
                        .autoSubscribed(createMembershipDTO.isAutoSubscribed())
                        .createdTimestamp(createdTimestamp)
                        .expirationTimestamp(ApplicationUtils
                                .calculateOrderItemExpirationTimestamp(
                                        orderItemDTO.getPricingPlan(),
                                        createdTimestamp))
                        .orderId(orderId)
                        .build());
        System.out.println(createMembershipDTO.getResourceItems());
        for (OrderItemDTO resourceItem : createMembershipDTO.getResourceItems()) {
            IOrderItemSaveService orderItemSaveService = this.getOrderItemSaveService(resourceItem);
            orderItemSaveService.save(resourceItem, account, createdTimestamp, orderId,
                    savedMembership.getId(), resourceItem.getItemType().name().toLowerCase());
        }
    }

    public IOrderItemSaveService getOrderItemSaveService(OrderItemDTO orderItemDTO) {
        if (orderItemDTO instanceof CreateAdsDTO) {
            return adsService;
        } else if (orderItemDTO instanceof CreateBoothDTO) {
            return boothService;
        } else if (orderItemDTO instanceof CreateJobDTO) {
            return jobsService;
        }

        System.out.println(orderItemDTO.toString());
        throw new RuntimeException("Invalid order item type!");
    }

    public void updateMembershipSubscription(Integer membershipId, Boolean autoSubscribed) {
        membershipRepository.updateAutoSubscribed(membershipId, autoSubscribed);
    }
}
