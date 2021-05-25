package com.austinhub.apiservice.model.po;

import com.austinhub.apiservice.model.CategoryType;
import java.io.Serializable;
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

/**
 * @Author: bill wang
 * @Date: 2021/04/16 10:19
 */
@Data
@Entity
@Table (name = "category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

	private static final long serialVersionUID =  2619877578856632239L;

   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

   	@Column(name = "name" )
	private String name;

   	@Column(name = "link")
	private String link;

   	@Column(name = "displayName")
	private String displayName;

   	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private CategoryType categoryType;

}
