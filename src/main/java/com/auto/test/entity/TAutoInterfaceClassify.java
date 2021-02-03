package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * (TAutoInterfaceClassify)实体类
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Data
@TableName("t_auto_interface_classify")
@ApiModel(value = "TAutoInterfaceClassify", description = "接口分组")
public class TAutoInterfaceClassify implements Serializable {
  private static final long serialVersionUID = 384188813645841406L;
  @ApiModelProperty(value = "id", hidden = true)
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  /**
   * 分组名称
   */
  @ApiModelProperty(value = "名称", required = true)
  private String name;
  @ApiModelProperty(value = "模块id", required = true)
  private String moduleId;
  @ApiModelProperty(value = "创建时间", hidden = true)
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
  @ApiModelProperty(value = "修改时间", hidden = true)
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
  
  @ApiModelProperty(hidden = true)
  public static List<TAutoInterfaceClassify> toInterfaceClassifyList(List<Tag> tags, String moduleId) {
    List<TAutoInterfaceClassify> tAutoInterfaceClassifyList = new ArrayList<>();
    if (tags != null && tags.size() > 0) {
      for (Tag tag : tags) {
        TAutoInterfaceClassify tAutoInterfaceClassify = new TAutoInterfaceClassify(tag.getName());
        tAutoInterfaceClassifyList.add(tAutoInterfaceClassify);
        tAutoInterfaceClassify.setModuleId(moduleId);
      }
    }
    return tAutoInterfaceClassifyList;
  }
  
  public TAutoInterfaceClassify(String mapKey) {
    String[] classifyNames = mapKey.split("/");
    String classifyName = classifyNames[0];
    if (classifyNames != null && classifyNames.length > 1) {
      classifyName = classifyName + "-" + classifyNames[1];
    }
    this.name = classifyName;
  }
  public TAutoInterfaceClassify(){
  
  }
}