package com.austinhub.apiservice.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipTypeDto {

    private String name;

    private String description;

    private List<ResourceLimit> resourceLimits;

    private Double monthlyPrice;

    private Double quarterlyPrice;

    private Double yearlyPrice;
}
