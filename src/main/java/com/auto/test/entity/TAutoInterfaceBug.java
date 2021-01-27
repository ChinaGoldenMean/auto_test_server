package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TAutoInterfaceBug)实体类
 *
 * @author litiewang
 * @since 2021-01-12 14:13:02
 */
@Data
@TableName("t_auto_interface_bug")
public class TAutoInterfaceBug implements Serializable {
  private static final long serialVersionUID = 995812428843606462L;
  
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  
  private String interfaceId;
  
  private String name;
  private String bugType;
  /**
   * 执行状态  1失败 2 已修复
   */
  private Integer status;
  
  private String resultId;
  
  private String remark;
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
  
}