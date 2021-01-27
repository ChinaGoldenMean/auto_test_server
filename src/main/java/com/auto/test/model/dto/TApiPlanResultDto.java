package com.auto.test.model.dto;

import com.auto.test.entity.TApiCaseResult;
import com.auto.test.entity.TApiResult;
import com.auto.test.entity.TAutoTestPlanApiResult;
import lombok.Data;

import java.util.List;

@Data
public class TApiPlanResultDto extends TAutoTestPlanApiResult {
  private static final long serialVersionUID = 1L;
  private List<TApiCaseResultDto> cases;
}
