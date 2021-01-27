package com.auto.test.dao;

import com.auto.test.entity.TApiResult;
import com.auto.test.model.dto.TApiCaseResultDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TApiResultDao extends BaseMapper<TApiResult> {

  List<TApiResult> resultByCaseId(String id);
}
