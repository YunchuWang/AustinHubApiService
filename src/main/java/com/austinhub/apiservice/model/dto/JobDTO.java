package com.austinhub.apiservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO extends OrderItemDTO {
    private String name;
    private String description;
    private String salary;
    private String phone;
    private String address;
    private String contact;
    private String categoryName;
    private String companyLink;
}
