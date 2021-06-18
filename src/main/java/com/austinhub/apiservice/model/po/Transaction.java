package com.austinhub.apiservice.model.po;

import java.math.BigDecimal;
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
