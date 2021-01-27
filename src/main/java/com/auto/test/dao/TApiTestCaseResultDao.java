package com.auto.test.dao;

import com.auto.test.entity.TApiCaseResult;
import com.auto.test.entity.TApiResult;
import com.auto.test.model.dto.TApiCaseResultDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TApiTestCaseResultDao extends BaseMapper<TApiCaseResult> {
  
  TApiCaseResultDto selectOneCaseResult(String id);
  List<TApiCaseResultDto> listCaseResultByPlanId(String id);
}
