package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.MembershipTypeDTO;
import com.austinhub.apiservice.model.dto.UpdateMembershipSubscriptionDTO;
import com.austinhub.apiservice.service.MembershipService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/memberships")
public class MembershipController {

    private MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping("/types")
    public ResponseEntity<List<MembershipTypeDTO>> findAllMembershipTypes() {
        return ResponseEntity.ok().body(membershipService.findAll());
    }

    @PostMapping("/{membershipId}/subscriptions")
    public ResponseEntity<Void> updateMembershipSubscription(
            @PathVariable(required = true) Integer membershipId,
            @Valid @RequestBody UpdateMembershipSubscriptionDTO updateMembershipSubscriptionDTO) {
        membershipService.updateMembershipSubscription(membershipId,
                updateMembershipSubscriptionDTO.getAutoSubscribed());
        return ResponseEntity.ok().build();
    }
}
