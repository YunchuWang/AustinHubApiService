package com.austinhub.apiservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class UpdateJobRequest {

    private Integer id;
    private String name;
    private String description;
    private String salary;
    private String phone;
    private String address;
    private String contact;
    private String categoryName;
    private String companyLink;
}