package com.austinhub.apiservice.model.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
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
public class Resource implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId")
    private Account account;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "categoryName")
    private String categoryName;
    
    @Column(name = "membershipId")
    private Integer membershipId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expirationTimestamp")
    private Date expirationTimestamp;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdTimestamp")
    private Date createdTimestamp;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "resource_order",
            joinColumns = @JoinColumn(name = "resourceId"),
            inverseJoinColumns = @JoinColumn(name = "orderId"))
    private Set<Order> orders;

    @Column(name = "isArchived", columnDefinition = "boolean default false")
    private Boolean isArchived;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resourceTypeId")
    private ResourceType resourceType;
}
