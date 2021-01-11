package com.auto.test.model.po;

import com.auto.test.model.constant.DataSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Assert", description = "断言")
public class Assert implements Serializable {
  @ApiModelProperty(value = "数据来源", required = true)
  private String dataSource;
  @ApiModelProperty(value = "期望类型", required = true)
  private String extractType;
  @ApiModelProperty(value = "提取表达式")
  private String extractExpress;
  @ApiModelProperty(value = "期望关系", required = true)
  private Integer expectRelation;
  @ApiModelProperty(value = "期望值", required = true)
  private String expectValue;
  @ApiModelProperty(value = "顺序")
  private Integer regexNo;
  private static final long serialVersionUID = 1L;
  
}