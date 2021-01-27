package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TAutoTestPlanApiResult)实体类
 *
 * @author litiewang
 * @since 2021-01-07 09:46:49
 */
@Data
@TableName("t_auto_test_plan_api_result")
public class TAutoTestPlanApiResult implements Serializable {
  private static final long serialVersionUID = 956766962063142797L;
  
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  /**
   * 任务名称
   */
  @ApiModelProperty(value = "任务名称")
  private String name;
  /**
   * 任务id
   */
  @ApiModelProperty(value = "任务id")
  private String jobId;
  /**
   * 状态 0未执行 1执行中 2执行完成   3执行失败
   */
  @ApiModelProperty(value = "状态 0未执行 1执行中 2执行完成   3执行失败")
  private Integer status;
  /**
   * 用例集总数
   */
  @ApiModelProperty(value = "用例集总数")
  private Integer suiteTotalCount;
  /**
   * 成功数
   */
  @ApiModelProperty(value = "成功数")
  private Integer suiteSuccCount;
  /**
   * 失败数
   */
  @ApiModelProperty(value = "失败数")
  private Integer suiteFailCount;
  /**
   * 结束时间
   */
  @ApiModelProperty(value = "执行时间")
  private Long endTime;
  /**
   * 备注
   */
  @ApiModelProperty(value = "备注")
  private String remark;
  /**
   * createTime
   */
  @ApiModelProperty(value = "创建时间")
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
}