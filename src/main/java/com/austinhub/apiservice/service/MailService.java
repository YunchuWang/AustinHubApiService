package com.austinhub.apiservice.service;

import com.austinhub.common_models.EmailDTO;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@NoArgsConstructor
@AllArgsConstructor
public class MailService {

    private String supportEmail;

    public EmailDTO constructOrderConfirmationEmail(final String toEmail, String orderNum) {
        return EmailDTO.builder().from(supportEmail).to(Arrays.asList(toEmail))
                .subject("Austin Hub Order confirmation #" + orderNum)
                .text("Thank you for your order!").build();
    }
}
