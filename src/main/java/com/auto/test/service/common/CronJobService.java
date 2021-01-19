package com.auto.test.service.common;

import com.auto.test.entity.TAutoJob;
import com.auto.test.k8s.dto.SearchParamDTO;
import com.auto.test.k8s.model.CronJob;
import com.auto.test.model.bo.base.Page;
import io.kubernetes.client.openapi.models.V1beta1CronJob;

import java.util.List;

public interface CronJobService {
  /**
   * 根据查询对象获取定时任务集合
   *
   * @param paramVo
   * @return
   */
  Page<List<CronJob>> listCronJob(SearchParamDTO paramVo);
  
  /**
   * 读取定时任务
   *
   * @param nameSpace
   * @param name
   * @return
   */
  V1beta1CronJob readCronJob(String nameSpace, String name);
  
  Boolean createJob(String name, String selfLink);
  
  Boolean createCronJob(TAutoJob job);
  
  Boolean cronJobToJob(TAutoJob autoJob);
  
  Boolean suspendOrRecoverJob(TAutoJob autoJob);
}
