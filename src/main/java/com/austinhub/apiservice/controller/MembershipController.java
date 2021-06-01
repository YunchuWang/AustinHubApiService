package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.MembershipTypeDto;
import com.austinhub.apiservice.service.MembershipService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<MembershipTypeDto>> findAllMembershipTypes() {
        return ResponseEntity.ok().body(membershipService.findAll());
    }
}
