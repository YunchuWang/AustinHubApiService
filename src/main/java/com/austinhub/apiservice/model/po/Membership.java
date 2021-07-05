package com.austinhub.apiservice.model.po;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "membership")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId")
    private Account account;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "membershipTypeId", referencedColumnName = "id")
    private MembershipType membershipType;

    @Column(name = "autoSubscribed")
    private Boolean autoSubscribed;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expirationTimestamp")
    private Date expirationTimestamp;
    
    @Column(name = "isArchived", columnDefinition = "boolean default false")
    private Boolean isArchived;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdTimestamp")
    private Date createdTimestamp;

    @ManyToMany
    @JoinTable(
            name = "membership_order",
            joinColumns = @JoinColumn(name = "membershipId"),
            inverseJoinColumns = @JoinColumn(name = "orderId"))
    private Set<Order> orders;
}
