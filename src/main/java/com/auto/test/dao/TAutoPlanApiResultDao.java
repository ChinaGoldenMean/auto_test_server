package com.auto.test.dao;

import com.auto.test.entity.TAutoTestPlanApiResult;
import com.auto.test.model.dto.TApiCaseResultDto;
import com.auto.test.model.dto.TApiPlanResultDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * (TAutoPlanApiResultDao)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-21 15:50:39
 */
public interface TAutoPlanApiResultDao extends BaseMapper<TAutoTestPlanApiResult> {
  
  TApiPlanResultDto selectOnePlanResult(String id);
}