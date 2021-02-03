package com.auto.test.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;

@Data
@Accessors(chain = true)
public class RepositoryVo {
  private  String basePath ;
  private String username;
  private String password;
  private String codeUrl;
  private String moduleId;
}
