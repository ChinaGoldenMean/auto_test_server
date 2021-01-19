package com.auto.test.entity;

import com.auto.test.model.po.AssertResult;
import com.auto.test.model.po.BodyData;
import com.auto.test.model.po.WebHeader;
import com.auto.test.model.po.Query;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_auto_api_result")
public class TApiResult implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.ASSIGN_UUID)
  @ApiModelProperty(value = "接口返回表id" )
  private String id;
  
  @ApiModelProperty(hidden = true)
  private String planLogId;
  
  /**
   * 用例结果id
   */
  @ApiModelProperty(value = "用例结果id" )
  private String caseResultId;
  
  /**
   * 步骤id
   */
  @ApiModelProperty(value = "步骤Id" )
  private String stepId;
  
  /**
   * 步骤名称
   */
  @ApiModelProperty(value = "步骤名称" )
  private String stepName;
  
  /**
   * -1：异常失败，0：断言失败，1：成功,2:跳过
   */
  @ApiModelProperty(value = "-1：异常失败，0：断言失败，1：成功,2:跳过" )
  private Integer resultType;
  
  /**
   * 请求方式
   */
  @ApiModelProperty(value = "请求方式" )
  private String reqMethod;
  
  /**
   * 请求url
   */
  @ApiModelProperty(value = "请求url" )
  private String reqUrl;
  
  /**
   * 请求头
   */
  @ApiModelProperty(value = "请求头" )
  private List<WebHeader> reqHeaders;
  
  /**
   * 请求query
   */
  @ApiModelProperty(value = "请求query" )
  private List<Query> reqQuery;
  
  /**
   * 请求body的类型
   */
  @ApiModelProperty(value = "请求body的类型" )
  private String reqBodyType;
  
  /**
   * 请求body from-data格式
   */
  @ApiModelProperty(value = "请求body from-data格式" )
  private List<BodyData> reqBodyData;
  
  /**
   * 请求body json格式
   */
  @ApiModelProperty(value = "请求body json格式" )
  private String reqBodyJson;
  
  /**
   * 响应码
   */
  @ApiModelProperty(value = "响应码" )
  private Integer rspStatusCode;
  
  /**
   * 响应body类型
   */
  @ApiModelProperty(value = "响应body类型" )
  private String rspBodyType;
  
  /**
   * 响应体
   */
  @ApiModelProperty(value = "响应体" )
  private String rspBody;
  
  /**
   * 响应体长度
   */
  @ApiModelProperty(value = "响应体长度" )
  private Integer rspBodySize;
  
  /**
   * 响应的header
   */
  @ApiModelProperty(value = "响应的header" )
  private List<WebHeader> rspHeaders;
  
  /**
   * 响应断言
   */
  @ApiModelProperty(value = "响应断言" )
  private List<AssertResult> rspAsserts;
  
  /**
   * 响应时间（毫秒）
   */
  @ApiModelProperty(value = "响应时间" )
  private Long rspTime;
  
  @ApiModelProperty(value = "异常信息" )
  private String exception;
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
}