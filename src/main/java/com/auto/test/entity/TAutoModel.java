package com.auto.test.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.GsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import io.swagger.models.utils.PropertyModelConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.io.Serializable;

/**
 * (TAutoModel)实体类
 *
 * @author litiewang
 * @since 2020-12-29 16:24:52
 */
@Data
@TableName(value = "t_auto_model", autoResultMap = true)
public class TAutoModel implements Serializable {
  private static final long serialVersionUID = -36274950212377566L;
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  @TableField(typeHandler = FastjsonTypeHandler.class)
  private JSONObject model;
  
  private String moduleId;
  private String description;
  @ApiModelProperty(value = "类型", required = true, example = "0")
  private Integer type;
  private String name;
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
  
  public TAutoModel(Model model, String moduleId) {
    this.name = model.getTitle();
    this.description = model.getDescription();
    this.type = 1;
    
    PropertyFilter profilter = (object, name, value) -> {
      if (name.equals("vendorExtensions")) {
        // false表示字段将被排除在外
        return false;
      }
      return true;
    };
    String jsonStr = JSONObject.toJSONString(model, profilter);
    this.model = JSONObject.parseObject(jsonStr);
    this.moduleId = moduleId;
  }
  
  
}