package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (TRole)实体类
 *
 * @author litiewang
 * @since 2020-12-18 16:51:00
 */
@Data
@TableName("t_role")
public class TRole implements Serializable {
  private static final long serialVersionUID = 882645296371057742L;
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  
  private String name;
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
}