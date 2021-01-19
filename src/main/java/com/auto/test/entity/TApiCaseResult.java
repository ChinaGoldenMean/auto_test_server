package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_auto_testcase_api_result")
public class TApiCaseResult implements Serializable {
  @TableId(type = IdType.ASSIGN_UUID)
  @ApiModelProperty(value = "测试用例返回表id" )
  private String id;
  
  @ApiModelProperty(hidden = true)
  private String planLogId;
  
  /**
   * 用例集id
   */
  @ApiModelProperty(hidden = true)
  private String planId;
  @ApiModelProperty(value = "步骤Id" )
  private String suiteId;
  
  @ApiModelProperty(value = "步骤名称" )
  private String suiteName;
  
  /**
   * 用例id
   */
  @ApiModelProperty(value = "用例id" )
  private String caseId;
  
  /**
   * 用例名称
   */
  @ApiModelProperty(value = "用例名称" )
  private String caseName;
  
  /**
   * 执行状态 0成功 1失败 2跳过
   */
  @ApiModelProperty(value = "执行状态 0成功 1失败 2跳过" )
  private Integer status;
  
  @ApiModelProperty(value = "总数" )
  private Integer total;
  
  @ApiModelProperty(value = "成功数" )
  private Integer success;
  
  @ApiModelProperty(value = "失败数" )
  private Integer failed;
  
  @ApiModelProperty(value = "跳过数" )
  private Integer skipped;
  
  /**
   * 结束时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  private Date endTime;
  
  /**
   * 备注
   */
  @ApiModelProperty(value = "备注" )
  private String remark;
  
  /**
   * createTime
   */
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
  private static final long serialVersionUID = 1L;
}