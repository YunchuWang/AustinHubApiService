package com.austinhub.apiservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class UpdateAdsRequest {

    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private String categoryName;
    private String webLink;
    private String imageLink;
}