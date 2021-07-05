package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.dto.CreateAdsDTO;
import com.austinhub.apiservice.model.dto.CreateBoothDTO;
import com.austinhub.apiservice.model.dto.CreateJobDTO;
import com.austinhub.apiservice.model.dto.CreateMembershipDTO;
import com.austinhub.apiservice.model.dto.MembershipTypeDTO;
import com.austinhub.apiservice.model.dto.PlaceOrderItemDTO;
import com.austinhub.apiservice.model.dto.RenewOrderItemDTO;
import com.austinhub.apiservice.model.dto.ResourceLimit;
import com.austinhub.apiservice.model.enums.ItemType;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Membership;
import com.austinhub.apiservice.model.po.MembershipType;
import com.austinhub.apiservice.model.po.Order;
import com.austinhub.apiservice.model.po.Resource;
import com.austinhub.apiservice.model.po.ResourceType;
import com.austinhub.apiservice.repository.MembershipRepository;
import com.austinhub.apiservice.repository.MembershipTypeRepository;
import com.austinhub.apiservice.utils.ApplicationUtils;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MembershipService implements IOrderItemService {

    private final MembershipRepository membershipRepository;
    private final MembershipTypeRepository membershipTypeRepository;
    private final AdsService adsService;
    private final JobsService jobsService;
    private final BoothService boothService;

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
    public void save(PlaceOrderItemDTO placeOrderItemDTO, Account account,
            Date createdTimestamp, Order order, Integer membershipId,
            String resourceTypeName) {
        CreateMembershipDTO createMembershipDTO = (CreateMembershipDTO) placeOrderItemDTO;
        MembershipType membershipType = membershipTypeRepository
                .findMembershipTypeByName(createMembershipDTO.getMembershipType().getType());
        Membership savedMembership = membershipRepository
                .save(Membership.builder()
                        .membershipType(membershipType)
                        .account(account)
                        .isArchived(false)
                        .autoSubscribed(createMembershipDTO.isAutoSubscribed())
                        .createdTimestamp(createdTimestamp)
                        .expirationTimestamp(ApplicationUtils
                                .calculateOrderItemExpirationTimestamp(
                                        placeOrderItemDTO.getPricingPlan(),
                                        createdTimestamp))
                        .orders(Set.of(order))
                        .build());
        for (PlaceOrderItemDTO resourceItem : createMembershipDTO.getResourceItems()) {
            IOrderItemService orderItemSaveService = this.getOrderItemSaveService(resourceItem);
            orderItemSaveService.save(resourceItem, account, createdTimestamp, order,
                    savedMembership.getId(), resourceItem.getItemType().name().toLowerCase());
        }
    }

    public void renew(RenewOrderItemDTO renewOrderItemDTO, Order order) {
        final Membership membership =
                membershipRepository.getOne(renewOrderItemDTO.getItemId());
        if (membership == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("No membership found with id %d", renewOrderItemDTO.getItemId()));
        }

        // update resource order and expiration time
        membership.setExpirationTimestamp(ApplicationUtils
                .calculateOrderItemExpirationTimestamp(renewOrderItemDTO.getPricingPlan(),
                        membership.getExpirationTimestamp()));
        membership.getOrders().add(order);

        membershipRepository.save(membership);
    }

    public IOrderItemService getOrderItemSaveService(PlaceOrderItemDTO placeOrderItemDTO) {
        if (placeOrderItemDTO instanceof CreateAdsDTO) {
            return adsService;
        } else if (placeOrderItemDTO instanceof CreateBoothDTO) {
            return boothService;
        } else if (placeOrderItemDTO instanceof CreateJobDTO) {
            return jobsService;
        }

        throw new RuntimeException("Invalid order item type!");
    }

    public IOrderItemService getOrderItemRenewService(RenewOrderItemDTO renewOrderItemDTO) {
        if (renewOrderItemDTO.getItemType().equals(ItemType.ADS)) {
            return adsService;
        } else if (renewOrderItemDTO.getItemType().equals(ItemType.BOOTH)) {
            return boothService;
        } else if (renewOrderItemDTO.getItemType().equals(ItemType.JOB)) {
            return jobsService;
        }

        throw new RuntimeException("Invalid order item type!");
    }

    public void renew(Order order, RenewOrderItemDTO renewOrderItemDTO) {

    }
}
