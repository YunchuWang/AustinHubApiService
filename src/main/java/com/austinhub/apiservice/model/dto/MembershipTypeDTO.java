package com.austinhub.apiservice.model.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipTypeDTO implements Serializable {

    private String name;

    private String description;

    private List<ResourceLimit> resourceLimits;

    private Double monthlyPrice;

    private Double quarterlyPrice;

    private Double yearlyPrice;
}
