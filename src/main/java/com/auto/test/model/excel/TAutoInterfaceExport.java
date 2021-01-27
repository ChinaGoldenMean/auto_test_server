package com.auto.test.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONArray;
import com.auto.test.entity.TAutoInterface;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (TAutoInterfaceExcel)实体类
 *
 * @author litiewang
 * @since 2020-12-22 11:26:09
 */
@Data
public class TAutoInterfaceExport implements Serializable {
  private static final long serialVersionUID = 224849586570733243L;
  
  @ExcelProperty("id")
  private String id;
  /**
   * 接口分类id
   */
  @ExcelProperty("分组id")
  private String classifyId;
  /**
   * 接口名称
   */
  @ExcelProperty("接口名称")
  private String name;
  
  @ExcelProperty("方法")
  private String method;
  /**
   * 请求路径
   */
  @ExcelProperty("请求路径")
  private String path;
  @ExcelProperty("域名")
  private String domain;
  /**
   * 类型 0公共接口  业务流接口
   */
  @ExcelProperty("请求类型 类型默认0，代表普通接口")
  private Integer type;
  
  /**
   * 备注
   */
  @ExcelProperty("备注")
  private String remark;
  
  /**
   * 状态 0未完成 1已完成
   */
  @ExcelProperty("状态 未完成0，已完成1")
  private Integer status;
  
  /**
   * 请求头
   */
  @ExcelProperty(value = "请求头")
  private String reqHeader;
  
  /**
   * 请求query
   */
  @ExcelProperty(value = "请求query")
  private String reqQuery;
  
  /**
   * 请求body from-data格式
   */
  @ExcelProperty(value = "请求body from-data格式")
  private String reqBodyData;
  
  /**
   * 请求body json格式
   */
  @ExcelProperty("请求body json格式")
  private String reqBodyJson;
  
  /**
   * 请求body的类型
   */
  @ExcelProperty("请求body的类型")
  private String reqBodyType;
  /**
   * 请求断言
   */
  @ExcelProperty("请求断言")
  private String reqAssert;
  
  /**
   * 调试响应对象
   */
  @ExcelProperty("调试响应对象")
  private String debugRsp;
  @ExcelProperty("创建时间")
  private Date createTime;
  
  /**
   * createBy
   */
  @ExcelProperty("创建人")
  private String createBy;
  /**
   * updateBy
   */
  @ExcelProperty("修改人")
  private String updateBy;
  
  @ExcelProperty("修改时间")
  private Date updateTime;
 public TAutoInterfaceExport(TAutoInterface autoInterface){
   
   BeanUtils.copyProperties(autoInterface, this);
  this.reqAssert = JSONArray.toJSONString(autoInterface.getReqAssert());
   this.reqHeader = JSONArray.toJSONString(autoInterface.getReqHeader());
   this.reqQuery = JSONArray.toJSONString(autoInterface.getReqQuery());
   this.reqBodyData = JSONArray.toJSONString(autoInterface.getReqBodyData());
   this.reqHeader = JSONArray.toJSONString(autoInterface.getReqHeader());
 }
  public static List<TAutoInterfaceExport> toList(List<TAutoInterface> list){
    List<TAutoInterfaceExport> interfaceExcels = new ArrayList<>();
    if(list!=null){
      list.stream().forEach(autoInterface -> {
        TAutoInterfaceExport interfaceExcel = new TAutoInterfaceExport(autoInterface);
        interfaceExcels.add(interfaceExcel);
      });
   }
    return interfaceExcels;
 }
 
}