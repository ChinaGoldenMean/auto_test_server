package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TAutoManageEnv)实体类
 *
 * @author litiewang
 * @since 2020-12-30 16:00:37
 */
@Data
@TableName("t_auto_manage_env")
@ApiModel(value = "TAutoManageEnv", description = "环境信息")
public class TAutoManageEnv implements Serializable {
  private static final long serialVersionUID = -82308939223363919L;
  /**
   * 主键
   */
  @ApiModelProperty(value = "id", hidden = true)
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 显示环境名称
   */
  @ApiModelProperty(value = "环境名称", required = true)
  private String envName;
  /**
   * kubectl操作所需文件内容
   */
  @ApiModelProperty(value = "k8sConfig", required = true)
  private String k8sConfig;
  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间", hidden = true)
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
  @ApiModelProperty(value = "修改时间", hidden = true)
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
  
}