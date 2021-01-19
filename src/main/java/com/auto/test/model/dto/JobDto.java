package com.auto.test.model.dto;

import com.auto.test.entity.TAutoJob;
import com.auto.test.entity.TJobSuiteApi;
import lombok.Data;

import java.util.List;

@Data
public class JobDto extends TAutoJob {
  private static final long serialVersionUID = 1L;
  private List<TJobSuiteApi> apiSuiteList;
  
}
