package com.austinhub.apiservice.cronjobs;

import com.austinhub.apiservice.model.enums.OrderStatus;
import com.austinhub.apiservice.model.po.Account;
import com.austinhub.apiservice.model.po.Order;
import com.austinhub.apiservice.model.po.Transaction;
import com.austinhub.apiservice.repository.AccountRepository;
import com.austinhub.apiservice.repository.OrderRepository;
import com.austinhub.apiservice.repository.TransactionRepository;
import com.austinhub.common_models.EmailDTO;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Transaction.Status;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

// Every day, scan submitted for settlement transactions and update the order status
// If declined, change order status to declined and send email
// If completed, do nothing
// if still submitted for settlement, decline order and send email
@Slf4j
public class TransactionStatusScannerJob implements Job {

    @Autowired
    private BraintreeGateway braintreeGateway;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${support.email}")
    private String supportEmail;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Starting transaction status scanner job...");
        Date date = new Date();
        List<Transaction> inProgressTransactions =
                transactionRepository.findByStatus(Transaction.IN_PROGRESS_STATUS);
        log.info("Number of transactions to scan : {}", inProgressTransactions.size());
        if (!inProgressTransactions.isEmpty()) {
            inProgressTransactions.forEach(transaction -> {
                updateTransactionStatus(date, transaction);
            });
        }
        log.info("Transaction status scanner job completed.");
    }

    private void updateTransactionStatus(Date date, Transaction transaction) {
        com.braintreegateway.Transaction braintreeTransaction = null;
        try {
            braintreeTransaction = braintreeGateway.transaction().find(
                    transaction.getExternalId());
            log.info("Transaction {} latest status is {}", transaction.getExternalId(),
                     braintreeTransaction.getStatus());
            if (isTransactionDeclined(transaction, braintreeTransaction, date)) {
                // log, update tran/order and send email
                log.info("Transaction {} is declined due to {}",
                         transaction.getExternalId(), transaction.getStatus());

                log.info("Updating transaction/order status to {}", OrderStatus.DECLINED);
                transactionRepository.updateStatus(transaction.getId(),
                                                   braintreeTransaction.getStatus());
                final Order order = orderRepository.getOne(transaction.getOrderId());
                order.setStatus(OrderStatus.DECLINED);
                orderRepository.save(order);

                this.sendDeclinedOrderEmail(order.getAccount(), order.getOrderNumber(),
                                            braintreeTransaction.getStatus(),
                                            transaction.getExpiryTime().before(date));
            } else {
                log.info("Transaction {} is completed",
                         transaction.getExternalId());
                transactionRepository.updateStatus(transaction.getId(),
                                                   braintreeTransaction.getStatus());
            }
        } catch (Exception e) {
            log.error(String.format("Failed to find transaction %s due to :",
                                    transaction.getExternalId()), e);
        }
    }

    private boolean isTransactionDeclined(Transaction transaction,
            com.braintreegateway.Transaction braintreeTransaction, Date date) {
        return Transaction.IN_PROGRESS_STATUS.contains(braintreeTransaction.getStatus())
                && transaction.getExpiryTime().before(date) || Transaction.DECLINED_STATUS
                .contains(braintreeTransaction.getStatus());
    }

    private void sendDeclinedOrderEmail(final Account account, final String orderNumber,
            final Status status, final boolean isExpired) {
        final String subject = String.format("Your order %s at AustinHub is declined",
                                             orderNumber);
        // Construct email
        EmailDTO emailDTO =
                EmailDTO.builder().from(supportEmail)
                        .to(List.of(account.getEmail()))
                        .subject(subject)
                        .text(String.format("Hi %s, \nThis is a reminder that %s due to \n"
                                                    + "Please log in to place an new order them.",
                                            account.getUsername(), subject,
                                            isExpired
                                                    ? "your payment is not approved beyond expiry"
                                                    + " time"
                                                    :
                                                            status)).build();

        // Send reminder email to the account email
        kafkaTemplate.send("email", emailDTO);
        log.info("Order declined email sent for account {}", account.getUsername());
    }
}
