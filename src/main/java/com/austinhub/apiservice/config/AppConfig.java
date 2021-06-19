package com.austinhub.apiservice.config;

import com.austinhub.apiservice.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${support.email}")
    private String supportEmail;

    @Bean
    public MailService mailService() {
        return new MailService(supportEmail);
    }
}
