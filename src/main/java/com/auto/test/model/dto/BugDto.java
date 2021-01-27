package com.auto.test.model.dto;

import com.auto.test.dao.TApiInterfaceBugDao;
import com.auto.test.entity.TApiResult;
import com.auto.test.entity.TAutoInterfaceBug;
import com.auto.test.entity.TAutoJob;
import com.auto.test.entity.TJobSuiteApi;
import lombok.Data;

@Data
public class BugDto {
  private static final long serialVersionUID = 1L;
  private TAutoInterfaceBug bug;
  private TApiResult result;
  
  public BugDto(TAutoInterfaceBug bug, TApiResult result) {
    this.bug = bug;
    this.result = result;
  }
}
