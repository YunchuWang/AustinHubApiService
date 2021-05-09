package com.austinhub.apiservice.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: zhangjian
 * @Date: 2021/04/16 10:21
 */
@Data
@Entity
@Table (name = "category_relation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRelation implements Serializable {

	private static final long serialVersionUID =  400395205547335973L;

   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

   	@Column(name = "category_id" )
	private Integer categoryId;

   	@Column(name = "subcategory_id" )
	private Integer subcategoryId;

}
