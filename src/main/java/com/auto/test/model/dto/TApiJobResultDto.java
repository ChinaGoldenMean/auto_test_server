package com.auto.test.model.dto;

import com.auto.test.entity.TApiCaseResult;
import com.auto.test.entity.TApiResult;
import com.auto.test.entity.TAutoTestPlanApiResult;
import com.auto.test.model.dto.TApiCaseResultDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TApiJobResultDto implements Serializable {
  private static final long serialVersionUID = 1L;
  private TAutoTestPlanApiResult planApiResult;
  private List<TApiCaseResultDto> caseResultLists;
}
