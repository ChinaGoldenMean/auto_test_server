package com.auto.test.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ParamList", description = "列表参数")
public class ParamList extends PageParamBase {
  /**
   * 接口名称
   */
  @ApiModelProperty(value = "名称,字段排序为name,create_time,update_time")
  protected String name;
}
