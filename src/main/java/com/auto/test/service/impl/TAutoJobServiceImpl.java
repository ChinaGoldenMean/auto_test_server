package com.auto.test.service.impl;

import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.CronPattern;
import com.auto.test.common.exception.ServiceException;
import com.auto.test.dao.TAutoJobDao;
import com.auto.test.dao.TJobSuiteApiDao;
import com.auto.test.entity.*;
import com.auto.test.model.dto.JobDto;
import com.auto.test.service.*;
import com.auto.test.service.common.CronJobService;
import com.auto.test.service.common.K8sApiService;
import com.auto.test.service.common.NamespaceService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1beta1CronJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TAutoJobService)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Service
public class TAutoJobServiceImpl extends ServiceImpl<TAutoJobDao, TAutoJob> implements TAutoJobService {
  @Resource
  private TJobSuiteApiDao jobSuiteApiDao;
  @Resource
  private TJobSuiteApiService suiteApiService;
  
  @Resource
  private TAutoJobDao tAutoJobDao;
  @Resource
  private TestCaseInterfaceService caseInterfaceService;
  @Resource
  private NamespaceService namespaceService;
  @Resource
  private CronJobService cronJobService;
  
  @Autowired
  K8sApiService k8sApi;
  
  @Transactional
  @Override
  public Boolean saveOrUpdateJob(JobDto job) {
    
    if (!checkCronExpressionIsValid(job.getCronExpression())) {
      throw new ServiceException("表达式校验失败！！！");
    }
    
    List<TJobSuiteApi> apiSuiteList = job.getApiSuiteList();
    //
    saveOrUpdate(job);
    if (apiSuiteList != null && apiSuiteList.size() > 0) {
      apiSuiteList.stream().forEach(apiSuite -> {
        apiSuite.setJobId(job.getId());
        TAutoTestcase autoTestcase = caseInterfaceService.getById(apiSuite.getTestcaseId());
        if (autoTestcase == null) {
          throw new ServiceException("该测试用例找不到！！！");
        }
       
      });
  
     //先删除，再修改与增加
        QueryWrapper<TJobSuiteApi> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_id",job.getId());
        suiteApiService.remove(queryWrapper);
     
      
      suiteApiService.saveOrUpdateBatch(apiSuiteList);
      V1Namespace v1Namespace = namespaceService.nameSpacesByName(job.getModuleId());
      
      //如果命名空间为Null,则创建，否则直接创建cron任务
      if (v1Namespace == null) {
        namespaceService.createNamespace(job.getModuleId());
      }
      //如果已经存在则不处理。因为cron处理的数据是从数据库获取的。
      V1beta1CronJob v1beta1CronJob = cronJobService.readCronJob(job.getModuleId(), job.getId());
      if (v1beta1CronJob == null) {
        cronJobService.createCronJob(job);
      }
      
      return true;
    }
    
    return false;
  }
  
  @Override
  public Boolean deleteJob(String id) {
    QueryWrapper<TJobSuiteApi> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("job_id", id);
    if (jobSuiteApiDao.delete(queryWrapper) > 0 && removeById(id)) {
      return true;
    }
    return false;
  }
  
  @Override
  public Boolean runJob(String id) {
    TAutoJob autoJob = getById(id);
    return cronJobService.cronJobToJob(autoJob);
  }
  
  @Override
  @Transactional
  public Boolean suspendOrRecoverJob(String id) {
    TAutoJob autoJob = getById(id);
    if (cronJobService.suspendOrRecoverJob(autoJob)) {
      Integer status = autoJob.getStatus()==0?1:0;
      autoJob.setStatus(status);
      saveOrUpdate(autoJob);
      return true;
    }
    
    return false;
  }
  
  @Override
  public boolean checkCronExpressionIsValid(String cronExpression) {
    try {
      //校验表达式
      new CronPattern(cronExpression);
    } catch (CronException e) {
      return false;
    }
    return true;
  }
}