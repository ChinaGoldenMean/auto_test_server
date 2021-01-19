package com.auto.test.service;

import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoJob;
import com.auto.test.entity.TJobSuiteApi;
import com.auto.test.model.dto.JobDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (TAutoJobService)表服务接口
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
public interface TAutoJobService extends IService<TAutoJob> {
  Boolean saveOrUpdateJob(JobDto job);
  
  Boolean deleteJob(String id);
  
  Boolean runJob(String id);
  
  Boolean suspendOrRecoverJob(String id);
  
  boolean checkCronExpressionIsValid(String cronExpression);
}