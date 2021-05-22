package com.austinhub.apiservice.model.po;

import com.austinhub.apiservice.validator.ExtendedEmailValidator;
import com.austinhub.apiservice.validator.Mobile;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ads")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "resource_id", nullable = false)
    private long resourceId;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    @Mobile
    private String phone;

    @Column(name = "email")
    @ExtendedEmailValidator
    private String email;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "web_link")
    private String webLink;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "created_timestamp")
    private Timestamp createdTimestamp;
}
