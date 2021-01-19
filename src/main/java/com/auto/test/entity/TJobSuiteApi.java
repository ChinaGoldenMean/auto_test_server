package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (TJobSuiteApi)实体类
 *
 * @author litiewang
 * @since 2021-01-06 17:31:12
 */
@Data
@TableName("t_job_suite_api")
@ApiModel(value = "TJobSuiteApi", description = "任务步骤")
public class TJobSuiteApi implements Serializable {
  private static final long serialVersionUID = -13937069889700024L;
  
  @ApiModelProperty(value = "id", hidden = true)
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  
  @ApiModelProperty(value = "任务id", hidden = true)
  private String jobId;
  @ApiModelProperty(value = "测试用例id")
  private String testcaseId;
  @ApiModelProperty(value = "测试用例name")
  private String name;
  @ApiModelProperty(value = "顺序", required = true)
  private Integer sort;
  /**
   * 1:有效，2：无效
   */
  @ApiModelProperty(value = "1:有效，2：无效", required = true)
  private Integer isValid;
  
}