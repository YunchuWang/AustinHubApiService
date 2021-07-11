package com.austinhub.apiservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormDTO {

    private String accountName;
    private String email;
    private String subject;
    private String message;
}
