package com.auto.test.model.po;

import com.alibaba.fastjson.JSONObject;
import com.auto.test.model.postman.PostmanFormData;
import com.auto.test.model.postman.PostmanUrlEncoded;
import com.github.apigcc.core.common.postman.Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "BodyData", description = "body键值对.")
public class BodyData implements Serializable {
  private String key;
  private Object value;
  private static final long serialVersionUID = 1L;
  public BodyData(){
  
  }
  public BodyData(Parameter parameter){
    BeanUtils.copyProperties(parameter, this);
    
  }
  public BodyData(PostmanFormData formDataBody){
    BeanUtils.copyProperties(formDataBody, this);

  }
  public BodyData(PostmanUrlEncoded encodedBody){
    BeanUtils.copyProperties(encodedBody, this);

  }
  
  public static List<BodyData> json2BodyDataList(JSONObject json) {
    List<BodyData> dataArrayList = new ArrayList<>();
    if (json.isEmpty()) {
      return null;
    }
//    for (Map.Entry<String, Object> entry : json.entrySet()) {
//      BodyData query = new BodyData();
//      query.setKey(entry.getKey());
//      query.setValue((String) entry.getValue());
//      queryList.add(query);
//    }
    BodyData bodyData = new BodyData();
    bodyData.setKey("form");
    bodyData.setValue(json.toJSONString());
    dataArrayList.add(bodyData);
    return dataArrayList;
  }
}