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
public class TAutoInterfaceImport implements Serializable {
  private static final long serialVersionUID = 224849586570733243L;
  
  /**
   * 接口分类id
   */
  @ExcelProperty("分组名称")
  private String classifyName;
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
  @ExcelProperty("域名")
  private String domain;
  @ExcelProperty("请求路径")
  private String path;
  public static List<TAutoInterface>  toList(List<TAutoInterfaceImport> interfaceExports){
    List<TAutoInterface> autoInterfaces = new ArrayList<>();
    if(interfaceExports!=null){
      interfaceExports.stream().forEach(interfaceExport -> {
        TAutoInterface interfaceExcel = new TAutoInterface(interfaceExport);
        autoInterfaces.add(interfaceExcel);
      });
    }
    return autoInterfaces;
  }
}