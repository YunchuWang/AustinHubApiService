package com.austinhub.apiservice.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: zhangjian
 * @Date: 2021/04/16 10:20
 */
@Data
@Entity
@Table (name = "subcategory")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subcategory implements Serializable {

	private static final long serialVersionUID =  5896955428561678680L;

   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

   	@Column(name = "name" )
	private String name;

}
