package com.auto.test.dao;

import com.auto.test.entity.TAutoJob;
import com.auto.test.entity.TAutoModel;
import com.auto.test.entity.TJobSuiteApi;
import com.auto.test.model.dto.JobDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TAutoJobDao extends BaseMapper<TAutoJob> {
  
  List<TJobSuiteApi> jobSuiteListByid(String jobId);
  
  JobDto selectOneJob(String id);
}
