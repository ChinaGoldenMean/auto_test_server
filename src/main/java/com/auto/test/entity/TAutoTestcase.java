package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TAutoTestcase)实体类
 *
 * @author litiewang
 * @since 2020-12-23 15:33:56
 */
@Data
@TableName("t_auto_testcase")
@ApiModel(value = "TAutoTestcase", description = "测试用例")
public class TAutoTestcase implements Serializable {
  private static final long serialVersionUID = -71769208646379710L;
  @ApiModelProperty(hidden = true)
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  @ApiModelProperty(value = "用例名称", required = true)
  private String name;
  
  @ApiModelProperty(hidden = true)
  private String status;
  @ApiModelProperty(value = "用例所属模块id", required = true)
  private String moduleId;
  @ApiModelProperty(value = "用例所属模块名称", required = true)
  private String moduleName;
  @ApiModelProperty(hidden = true)
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(hidden = true)
  private Date updateTime;
  
}