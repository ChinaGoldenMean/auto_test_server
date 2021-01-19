package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (TAutoJob)实体类
 *
 * @author litiewang
 * @since 2020-12-31 16:58:58
 */
@Data
@TableName("t_auto_yaml_default")
@ApiModel(value = "TAutoYaml", description = "yaml")
public class TAutoYaml implements Serializable {
  private static final long serialVersionUID = 482915594768792878L;
  
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  
  private String yaml;
  
  @ApiModelProperty(value = "创建时间", hidden = true)
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
  @ApiModelProperty(value = "修改时间", hidden = true)
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
  
}