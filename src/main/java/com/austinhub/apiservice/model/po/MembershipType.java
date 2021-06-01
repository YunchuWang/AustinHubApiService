package com.austinhub.apiservice.model.po;

import com.austinhub.apiservice.model.dto.MembershipTypeDto;
import com.austinhub.apiservice.model.dto.ResourceLimit;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "membership_type")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "monthlyPrice")
    private Double monthlyPrice;

    @Column(name = "quarterlyPrice")
    private Double quarterlyPrice;

    @Column(name = "yearlyPrice")
    private Double yearlyPrice;

    public MembershipTypeDto toDto(List<ResourceLimit> resourceLimits) {
        return MembershipTypeDto.builder()
                .name(name)
                .description(description)
                .resourceLimits(resourceLimits)
                .monthlyPrice(monthlyPrice)
                .quarterlyPrice(quarterlyPrice)
                .yearlyPrice(yearlyPrice)
                .build();
    }
}
