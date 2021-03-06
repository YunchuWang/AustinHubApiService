package com.austinhub.apiservice.model.po;

import com.austinhub.apiservice.validator.ExtendedEmailValidator;
import com.austinhub.apiservice.validator.Mobile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: bill wang
 * @Date: 2021/04/16 09:21
 */
@Data
@Entity
@Table (name = "booth")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booth implements Serializable {

	private static final long serialVersionUID =  7593034798875290855L;

   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

   	// TODO: hide account private info?
	@OneToOne(fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name = "resourceId", referencedColumnName = "id")
	private Resource resource;

	@NotBlank
   	@Column(name = "name" )
	private String name;

	@NotBlank
	@Column(name = "address" )
	private String address;

   	@Column(name = "phone" )
	@Mobile
	private String phone;

   	@Column(name = "email" )
	@ExtendedEmailValidator
	private String email;

   	@Column(name = "description" )
	private String description;

	@Column(name = "webLink" )
	private String webLink;

	@ManyToOne
	@JoinColumn(name = "categoryId", nullable = false)
	private Category category;
}
