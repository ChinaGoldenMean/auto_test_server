package com.auto.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONObject;
import com.auto.test.common.handler.typehandler.AssertListTypeHandler;
import com.auto.test.common.handler.typehandler.BodyDataListTypeHandler;
import com.auto.test.common.handler.typehandler.HeaderListTypeHandler;
import com.auto.test.common.handler.typehandler.QueryListTypeHandler;
import com.auto.test.config.excel.CustomConverter;
import com.auto.test.config.excel.HeaderCustomConverter;
import com.auto.test.config.excel.WebHeaderCustomConverter;
import com.auto.test.model.excel.TAutoInterfaceExport;
import com.auto.test.model.excel.TAutoInterfaceImport;
import com.auto.test.model.po.Assert;
import com.auto.test.model.po.BodyData;
import com.auto.test.model.po.Query;
import com.auto.test.model.po.WebHeader;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * (TAutoInterface)实体类
 *
 * @author litiewang
 * @since 2020-12-22 11:26:09
 */
@Data
@TableName("t_auto_interface")
@ApiModel(value = "TAutoInterface", description = "接口")
public class TAutoInterface implements Serializable {
  private static final long serialVersionUID = 224849586570733243L;
  @ApiModelProperty(value = "id", hidden = true)
  @ExcelProperty("id")
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  /**
   * 接口分类id
   */
  @ApiModelProperty(value = "分组id", required = true)
  @TableField("classify_id")
  @ExcelProperty("分组id")
  private String classifyId;
  /**
   * 接口名称
   */
  @ApiModelProperty(value = "接口名称", required = true)
  @ExcelProperty("接口名称")
  private String name;
  
  @ApiModelProperty(value = "方法", required = true)
  @ExcelProperty("方法")
  private String method;
  /**
   * 请求路径
   */
  @ApiModelProperty(value = "请求路径", required = true)
  @ExcelProperty("请求路径")
  private String path;
  @ApiModelProperty(value = "域名", required = true)
  @ExcelProperty("域名")
  private String domain;
  /**
   * 类型 0公共接口  业务流接口
   */
  @ApiModelProperty(value = "请求类型 类型默认0，代表普通接口", required = true, example = "0")
  @ExcelProperty("请求类型 类型默认0，代表普通接口")
  private Integer type;
  
  /**
   * 备注
   */
  @ApiModelProperty(value = "备注", required = true)
  @ExcelProperty("备注")
  private String remark;
  
  /**
   * 状态 0未完成 1已完成
   */
  @ApiModelProperty(value = "状态 未完成0，已完成1", required = true, example = "0")
  @ExcelProperty("状态 未完成0，已完成1")
  private Integer status;
  
  /**
   * 请求头
   */
  @TableField(typeHandler = HeaderListTypeHandler.class)
  @ExcelProperty(value = "请求头",converter = HeaderCustomConverter.class)
  @ApiModelProperty(value = "请求头", required = true)
  private List<WebHeader> reqHeader;
  
  /**
   * 请求query
   */
  @TableField(typeHandler = QueryListTypeHandler.class)
  @ExcelProperty(value = "请求query")
  @ApiModelProperty(value = "请求query", required = true)
  private List<Query> reqQuery;
  
  /**
   * 请求body from-data格式
   */
  @TableField(typeHandler = BodyDataListTypeHandler.class)
  @ExcelProperty(value = "请求body from-data格式")
  @ApiModelProperty(value = "请求body from-data格式", required = true)
  private List<BodyData> reqBodyData;
  
  /**
   * 请求body json格式
   */
  @ApiModelProperty(value = "请求body json格式", required = true)
  @ExcelProperty("请求body json格式")
  private String reqBodyJson;
  
  /**
   * 请求body的类型
   */
  @ApiModelProperty(value = "请求body的类型", required = true)
  @ExcelProperty("请求body的类型")
  private String reqBodyType;
  /**
   * 请求断言
   */
  @ApiModelProperty(value = "请求断言")
  @TableField(typeHandler = AssertListTypeHandler.class)
  @ExcelProperty(value = "请求断言")
  private List<Assert> reqAssert;
  
  /**
   * 调试响应对象
   */
  @ApiModelProperty(value = "调试响应对象", hidden = true)
  @ExcelProperty("调试响应对象")
  private String debugRsp;
  @ApiModelProperty(value = "创建时间", hidden = true)
  @ExcelProperty("创建时间")
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  
  /**
   * createBy
   */
  @ApiModelProperty(value = "创建人", hidden = true)
  @ExcelProperty("创建人")
  private String createBy;
  /**
   * updateBy
   */
  @ApiModelProperty(value = "修改人", hidden = true)
  @ExcelProperty("修改人")
  private String updateBy;
  
  @ApiModelProperty(value = "修改时间", hidden = true)
  @ExcelProperty("修改时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

//    public    TAutoInterface(Operation operation) {
//        List<Parameter> parameters = operation.getParameters();
//        if(parameters!=null&&parameters.size()>0){
//
//            for(Parameter parameter: parameters){
//                String in =  parameter.getIn();
//                if("body".equals(in)){
//                    this.reqBodyType="raw";
//                    BodyParameter bodyParameter = JSONObject.parseObject(parameter.toString(),BodyParameter.class);
//                    Model model = bodyParameter.getSchema();
//                    this.reqBodyJson =model.toString();
//                }else if("formData".equals(in)){
//                    this.reqBodyType="form";
//                    JSONObject jsonObject = JSONObject.parseObject(parameter.toString());
//                    jsonObject.remove("in");
//
//                    this.reqBodyData =BodyData.json2BodyDataList(jsonObject);
//                }else if("query".equals(in)){
//                    // autoInterface.setReqQuery();
//                    //  QueryParameter queryParameter = (QueryParameter)parameter;
//                    JSONObject jsonObject = JSONObject.parseObject(parameter.toString());
//                    jsonObject.remove("in");
//                    this.reqQuery=Query.json2QueryList(jsonObject);
//
//                }
//            }
//        }
//    }
 public TAutoInterface(TAutoInterfaceImport export){
  
   BeanUtils.copyProperties(export, this);
   this.type =0;
   this.status=0;
 }
 public TAutoInterface(){
 
 }
}