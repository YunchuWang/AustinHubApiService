package com.austinhub.apiservice.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/** @Author: zhangjian @Date: 2021/04/16 21:05 */
@Data
@Entity
@Table(name = "resource")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource implements Serializable {

  private static final long serialVersionUID = 2104034433129280889L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;
}
