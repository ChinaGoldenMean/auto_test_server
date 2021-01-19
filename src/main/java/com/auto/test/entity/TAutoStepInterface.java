package com.auto.test.entity;

import com.auto.test.common.handler.typehandler.*;
import com.auto.test.model.po.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * (TAutoStepInterface)实体类
 *
 * @author litiewang
 * @since 2020-12-23 15:45:40
 */
@Data
@TableName("t_auto_step_interface")
@ApiModel(value = "TAutoStepInterface", description = "用例步骤")
public class TAutoStepInterface implements Serializable {
  private static final long serialVersionUID = 908777159210538411L;
  
  @ApiModelProperty(hidden = true)
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  
  @ApiModelProperty(value = "步骤名称", required = true)
  private String name;
  
  @ApiModelProperty(hidden = true)
  private Integer type = 1;
  
  @ApiModelProperty(value = "测试用例id", hidden = true)
  private String testcaseId;
  /**
   * 状态：1启用 0禁用
   */
  @ApiModelProperty(value = "是否启用,1启用 0禁用", required = true, example = "0")
  private Integer status;
  
  @ApiModelProperty(value = "源接口id", required = true)
  private String sourceId;
  /**
   * 请求类型
   */
  @ApiModelProperty(value = "请求类型", required = true)
  private String method;
  /**
   * 请求的协议+域名
   */
  @ApiModelProperty(value = "请求的协议+域名", required = true)
  private String domain;
  /**
   * 请求路径
   */
  @ApiModelProperty(value = "请求路径", required = true)
  private String path;
  /**
   * api请求参数
   */
  @ApiModelProperty(value = "api请求参数")
  @TableField(typeHandler = ApiParamListTypeHandler.class)
  private List<ApiParam> apiParams;
  
  /**
   * 请求头
   */
  @TableField(typeHandler = HeaderListTypeHandler.class)
  @ApiModelProperty(value = "请求头", required = true)
  private List<WebHeader> reqHeader;
  /**
   * 请求query
   */
  @TableField(typeHandler = QueryListTypeHandler.class)
  @ApiModelProperty(value = "请求query")
  private List<Query> reqQuery;
  /**
   * 请求body from-data格式
   */
  @TableField(typeHandler = BodyDataListTypeHandler.class)
  @ApiModelProperty(value = "请求body from-data格式")
  private List<BodyData> reqBodyData;
  /**
   * 请求body json格式
   */
  @ApiModelProperty(value = "请求body json格式")
  private String reqBodyJson;
  /**
   * 请求body的类型
   */
  @ApiModelProperty(value = "请求body的类型,raw|text|form")
  private String reqBodyType;
  
  /**
   * 请求断言
   */
  @ApiModelProperty(value = "请求断言")
  @TableField(typeHandler = AssertListTypeHandler.class)
  private List<Assert> reqAssert;
  @ApiModelProperty(hidden = true)
  private String createBy;
  @ApiModelProperty(hidden = true)
  private Date createTime;
  @ApiModelProperty(hidden = true)
  private String updateBy;
  @ApiModelProperty(hidden = true)
  private Date updateTime;
  @ApiModelProperty(value = "顺序", example = "1")
  private Integer sort;
  
}