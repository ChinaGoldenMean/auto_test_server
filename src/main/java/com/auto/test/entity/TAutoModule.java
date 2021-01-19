package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (TAutoModule)实体类
 *
 * @author litiewang
 * @since 2020-12-18 15:15:46
 */
@Data
@TableName("t_auto_module")
public class TAutoModule implements Serializable {
  private static final long serialVersionUID = -23373785484479148L;
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  
  private String moduleName;
  
  private String centerId;
  
  private Integer isvalid;
  
  private String loginInterface;
  
  //暂时使用object
  private Object authenticationData;
  
}