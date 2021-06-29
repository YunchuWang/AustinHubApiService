package com.austinhub.apiservice.model.po;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table (name = "job")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "salary")
    private String salary;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    // TODO: hide account private info?
    @OneToOne
    @MapsId
    @JoinColumn(name = "resourceId", referencedColumnName = "id")
    private Resource resource;

    @Column(name = "companyLink")
    private String companyLink;
}
