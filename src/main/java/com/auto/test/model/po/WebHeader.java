package com.auto.test.model.po;

import com.auto.test.model.postman.PostmanHeader;
import com.github.apigcc.core.schema.Header;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "WebHeader", description = "请求头")
public class WebHeader implements Serializable {
  
  private String key;
  private String value;
  private static final long serialVersionUID = 1L;
  
  public WebHeader(Header header){
    BeanUtils.copyProperties(header, this);
    
  }
  public WebHeader(PostmanHeader header){
    BeanUtils.copyProperties(header, this);
    
  }
  
  public WebHeader(String ContentTypeValue) {
    this.key = "Content-Type";
    this.value = ContentTypeValue;
  }
}