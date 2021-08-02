package com.austinhub.apiservice.model.po;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "resource_type")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tableName")
    private String tableName;

    @Column(name = "description")
    private String description;

    @Column(name = "monthlyPrice")
    private Double monthlyPrice;

    @Column(name = "quarterlyPrice")
    private Double quarterlyPrice;

    @Column(name = "yearlyPrice")
    private Double yearlyPrice;
}
