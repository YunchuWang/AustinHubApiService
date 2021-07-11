package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.ContactFormDTO;
import com.austinhub.apiservice.model.dto.MembershipTypeDTO;
import com.austinhub.apiservice.model.dto.SaveAdsRequest;
import com.austinhub.apiservice.service.MailService;
import com.austinhub.apiservice.service.MembershipService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/emails")
public class EmailController {
    private final KafkaTemplate kafkaTemplate;
    private final MailService mailService;

    public EmailController(KafkaTemplate kafkaTemplate, MailService mailService) {
        this.kafkaTemplate = kafkaTemplate;
        this.mailService = mailService;
    }

    @PostMapping("/contact")
    public ResponseEntity<Void> sendContact(@Valid @RequestBody ContactFormDTO contactFormDTO) {
        kafkaTemplate.send("email", mailService.constructContactUsEmail(contactFormDTO));
        return ResponseEntity.noContent().build();
    }
}
