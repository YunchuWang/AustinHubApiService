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
public class CreateBoothDTO extends PlaceOrderItemDTO {
    private String name;
    private String description;
    private Integer salary;
    private String phone;
    private String email;
    private String address;
    private String contact;
    private String categoryName;
    private String webLink;
}
