package com.austinhub.apiservice.model.po;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "resource")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId")
    private Account account;

    @Column(name = "membershipId")
    private Integer membershipId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expirationTimestamp")
    private Date expirationTimestamp;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdTimestamp")
    private Date createdTimestamp;

    @Column(name = "orderId")
    private Integer orderId;

    @Column(name = "isArchived")
    private Boolean isArchived;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resourceTypeId")
    private ResourceType resourceType;
}
