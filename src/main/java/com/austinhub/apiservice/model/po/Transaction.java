package com.austinhub.apiservice.model.po;

import static com.braintreegateway.Transaction.Status.AUTHORIZATION_EXPIRED;
import static com.braintreegateway.Transaction.Status.AUTHORIZING;
import static com.braintreegateway.Transaction.Status.FAILED;
import static com.braintreegateway.Transaction.Status.GATEWAY_REJECTED;
import static com.braintreegateway.Transaction.Status.PROCESSOR_DECLINED;
import static com.braintreegateway.Transaction.Status.SETTLEMENT_DECLINED;
import static com.braintreegateway.Transaction.Status.SETTLING;
import static com.braintreegateway.Transaction.Status.SUBMITTED_FOR_SETTLEMENT;
import static com.braintreegateway.Transaction.Status.UNRECOGNIZED;
import static com.braintreegateway.Transaction.Status.VOIDED;

import com.braintreegateway.Transaction.Status;
import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "transaction")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    public static final Set<Status> DECLINED_STATUS = ImmutableSet
            .of(AUTHORIZATION_EXPIRED, FAILED, GATEWAY_REJECTED, PROCESSOR_DECLINED,
                    SETTLEMENT_DECLINED, UNRECOGNIZED, VOIDED);

    public static final Set<Status> IN_PROGRESS_STATUS = ImmutableSet
            .of(AUTHORIZING, SETTLING, SUBMITTED_FOR_SETTLEMENT);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "externalId")
    private String externalId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "createdTimestamp")
    private java.sql.Timestamp createdTimestamp;

    @Column(name = "expiryTime")
    private java.sql.Timestamp expiryTime;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private com.braintreegateway.Transaction.Status status;

    @Column(name = "orderId")
    private Integer orderId;

    @Column(name = "type")
    private String type;

    @Column(name = "merchantId")
    private String merchantId;
}
