package com.austinhub.apiservice.cronjobs;

import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.repository.AccountRepository;
import com.austinhub.apiservice.repository.MembershipRepository;
import com.austinhub.apiservice.repository.ResourceRepository;
import com.austinhub.common_models.EmailDTO;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
@DisallowConcurrentExecution
public class ExpirationEmailReminderJob implements Job {

    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Value("${support.email}")
    private String supportEmail;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Starting expiration email reminder job...");
        // Set expiration time as 2 week in advance from now
        Date expirationTime =
                Date.from(LocalDate.now().plusWeeks(2).atStartOfDay(ZoneId.of("UTC")).toInstant());
        // Find all accounts
        log.info("Finding all accounts...");
        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()) {
            log.warn("No account found in the system!");
            log.info("Expiration email reminder job completed.");
            return;
        }

        accounts.forEach(account -> {
            // find its unexpired membership or resources expiring soon
            boolean hasMembershipExpiring = membershipRepository
                    .existsUnexpiredMembership(account.getId(), expirationTime);
            boolean hasResourceExpiring = resourceRepository
                    .existsUnexpiredResource(account.getId(),
                                             expirationTime);
            if (hasMembershipExpiring || hasResourceExpiring) {
                sendRenewReminderEmail(account, hasMembershipExpiring, hasResourceExpiring);
            }
        });

        log.info("Expiration email reminder job completed.");
    }

    private void sendRenewReminderEmail(final Account account, final boolean hasMembershipExpiring,
            final boolean hasResourceExpiring) {
        StringBuilder renewalEmailTitle = createRenewEmailTitle(hasMembershipExpiring,
                                                                hasResourceExpiring);

        // Construct email
        EmailDTO emailDTO =
                EmailDTO.builder().from(supportEmail)
                        .to(List.of(account.getEmail()))
                        .subject(renewalEmailTitle.toString())
                        .text(String.format("Hi %s, \nThis is a reminder that %s \n"
                                                    + "Please log in to renew them.",
                                            account.getUsername(), renewalEmailTitle.toString()))
                        .build();

        // Send reminder email to the account email
        kafkaTemplate.send("email", emailDTO);
        log.info("Reminder email sent for account {}", account.getUsername());
    }

    @NotNull
    private StringBuilder createRenewEmailTitle(boolean hasMembership, boolean hasResource) {
        StringBuilder sb = new StringBuilder();

        sb.append("Your AustinHub ");
        if (hasMembership && hasResource) {
            sb.append("membership and resources ");
        } else if (hasMembership) {
            sb.append("membership ");
        } else {
            sb.append("resources ");
        }
        sb.append("are about to expire in 2 weeks");
        return sb;
    }
}
