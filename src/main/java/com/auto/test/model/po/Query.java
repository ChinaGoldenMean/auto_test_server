package com.auto.test.model.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.apigcc.core.common.postman.Parameter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Data
@ApiModel(value = "Query", description = "请求参数")
public class Query implements Serializable {
  private String key;
  private Object value;
  private String description;
  private boolean disabled = false;
  private String type;
  private static final long serialVersionUID = 1L;
  public static List<Query> rawToListQuery(String raw){
    List<Query> queryList = new ArrayList<>();
    if(!StringUtils.isEmpty(raw)){
      String parameter = raw.substring(raw.indexOf("?")+1);
      if(!StringUtils.isEmpty(parameter)){
        String[] parameters=parameter.split("&");
        if(parameters!=null&&parameters.length>0){
          for (int i=0;i<parameters.length;i++){
            String[] str =parameters[i].split("=");
            if(str!=null&&str.length>0){
              String key = str[0];
              String value="";
              if(str.length>1){
                  value = str[1];
              }
              
              Query query = new Query();
              query.setKey(key);
              if(value.indexOf("<")==-1&&value.indexOf(">")==-1){
                query.setValue(value);
              }
              queryList.add(query);
            }
           
          }
        }
      }
    }
    return queryList;
  }
  public Query(Parameter parameter){
    BeanUtils.copyProperties(parameter, this);
  
  }
//  public Query(PostmanQuery postmanQuery){
//    BeanUtils.copyProperties(postmanQuery, this);
//
//  }
  
  public static List<Query> json2QueryList(JSONObject json) {
    List<Query> queryList = new ArrayList<>();
    if (json.isEmpty()) {
      return null;
    }
//    for (Map.Entry<String, Object> entry : json.entrySet()) {
//      Query query = new Query();
//      query.setKey(entry.getKey());
//      query.setValue(entry.getValue());
//      queryList.add(query);
//    }
    Query query = new Query();
    query.setKey("query");
    query.setValue(json.toJSONString());
    queryList.add(query);
    return queryList;
  }
  
}