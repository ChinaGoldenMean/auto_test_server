package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TAutoJob)实体类
 *
 * @author litiewang
 * @since 2020-12-31 16:58:58
 */
@Data
@TableName("t_auto_job")
@ApiModel(value = "TAutoJob", description = "任务")
public class TAutoJob implements Serializable {
  private static final long serialVersionUID = 482915594768792878L;
  @ApiModelProperty(value = "id", hidden = true)
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  
  @ApiModelProperty(value = "任务名称", required = true)
  private String name;
  
  @ApiModelProperty(value = "模块id", required = true)
  private String moduleId;
  @ApiModelProperty(value = "任务执行表达式", required = true)
  private String cronExpression;
  
  @ApiModelProperty(value = "cron计划策略 ")
  private Integer misfirePolicy;
  
  @ApiModelProperty(value = "任务状态（0正常 1暂停） ")
  private Integer status;
  
  @ApiModelProperty(value = "0系统 1 UI自动化 2接口自动化", hidden = true)
  private String jobType;
  
  @ApiModelProperty(value = "备注 ")
  private String remark;
  
  @ApiModelProperty(value = "创建时间", hidden = true)
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
  @ApiModelProperty(value = "修改时间", hidden = true)
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
  
}