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
public class CreateAdsDTO extends OrderItemDTO {
    private String name;
    private String description;
    private Integer salary;
    private String phone;
    private String address;
    private String email;
    private String contact;
    private String categoryName;
    private String webLink;
    private String imageUploaded;
}
