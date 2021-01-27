package com.auto.test.service.common.impl;

import com.auto.test.common.exception.ServiceException;
import com.auto.test.entity.TAutoJob;
import com.auto.test.entity.TAutoYaml;
import com.auto.test.k8s.constant.ApiVersionEnum;
import com.auto.test.k8s.constant.K8sParam;
import com.auto.test.k8s.constant.KindEnum;
import com.auto.test.k8s.dto.SearchParamDTO;
import com.auto.test.k8s.model.CronJob;
import com.auto.test.k8s.constant.K8sParam.*;
import com.auto.test.k8s.model.Event;
import com.auto.test.k8s.model.Job;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.bo.base.ResultCode;
import com.auto.test.model.constant.K8sKind;
import com.auto.test.service.TAutoYamlService;
import com.auto.test.service.common.*;
import com.auto.test.utils.K8sSearch;
import com.auto.test.utils.K8sUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.BatchV1beta1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.conversion.Jobs;
import okhttp3.Call;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@org.springframework.stereotype.Service
public class CronJobServiceImpl extends K8sSearch implements CronJobService {
  @Autowired
  K8sApiService k8sApiService;
  @Autowired
  K8sService k8sService;
  @Autowired
  PodService podService;
  @Autowired
  EventsService eventsService;
  @Autowired
  JobService jobService;
  
  @Autowired
  TAutoYamlService yamlService;
  
  @Override
  public Page<List<CronJob>> listCronJob(SearchParamDTO paramVo) {
    BatchV1beta1Api batchV1beta1Api = k8sApiService.getBatchV1beta1Api();
    List<V1beta1CronJob> items = null;
    String nameSpace = k8sApiService.getNamespace();
    try {
      if (nameSpace.indexOf(K8sParam.NAMESPACE_ALL) == 0) {
        
        items = batchV1beta1Api.listCronJobForAllNamespaces(ListParam.allowWatchBookmarks, ListParam._continue, ListParam.fieldSelector, ListParam.labelSelector, ListParam.limit, ListParam.pretty, ListParam.resourceVersion, ListParam.timeoutSeconds, ListParam.watch
        ).getItems();
        
      } else {
        items = batchV1beta1Api.listNamespacedCronJob(nameSpace, ListParam.pretty, ListParam.allowWatchBookmarks, ListParam._continue, ListParam.fieldSelector, ListParam.labelSelector, ListParam.limit, ListParam.resourceVersion, ListParam.timeoutSeconds, ListParam.watch).getItems();
      }
    } catch (ApiException e) {
      throw new ServiceException(ResultCode.QUERY_CRON_JOB_FAIL);
    }
    
    K8sSearch search = new K8sSearch();
    List<V1beta1CronJob> list = pagingOrder(paramVo, items, V1beta1CronJob::getMetadata, nameSpace);
    List<CronJob> cronJobs = new ArrayList<>();
    if (list != null && list.size() > 0) {
      list.stream().forEach(v1beta1CronJob -> {
        
        CronJob cronJob = new CronJob().initCronJob(v1beta1CronJob);
        cronJobs.add(cronJob);
      });
    }
    Page<List<CronJob>> page = new Page(paramVo, cronJobs, getTotalItem(), new Long(cronJobs.size()));
    return page;
  }
  
  List<String> listJobByCronJob(V1beta1CronJob v1beta1CronJob) {
    List<V1ObjectReference> active = v1beta1CronJob.getStatus().getActive();
    List<String> jobNames = new ArrayList<>();
    if (active != null && !active.isEmpty()) {
      active.stream().forEach(v1ObjectReference -> {
        jobNames.add(v1ObjectReference.getName());
      });
    }
    return jobNames;
  }
  
  @Override
  public V1beta1CronJob readCronJob(String nameSpace, String name) {
    
    BatchV1beta1Api batchV1beta1Api = k8sApiService.getBatchV1beta1Api();
    V1beta1CronJob v1beta1CronJob = null;
    
    try {
      v1beta1CronJob = batchV1beta1Api.readNamespacedCronJob(name, nameSpace, ReadParam.pretty, ReadParam.exact, ReadParam.export);
    } catch (ApiException e) {
//      if (e.getCode() != 404) {
//        throw new ServiceException(ResultCode.QUERY_CRON_JOB_FAIL);
//      }
    }
    
    return v1beta1CronJob;
  }
  
  @Override
  public Boolean createJob(String name, String selfLink) {
    if (StringUtils.isEmpty(selfLink)) {
      throw new ServiceException(ResultCode.VALIDATE_FAILED);
    }
    String[] urls = selfLink.split("/");
    String nameSpace = urls[urls.length - 3];
    String cronJobName = urls[urls.length - 1];
    
    V1beta1CronJob v1beta1CronJob;
    BatchV1beta1Api batchV1beta1Api = k8sApiService.getBatchV1beta1Api();
    try {
      v1beta1CronJob = batchV1beta1Api.readNamespacedCronJob(cronJobName, nameSpace, ReadParam.pretty, ReadParam.exact, ReadParam.export);
      
      V1Job v1Job = new V1Job();
      V1ObjectMeta metadata = new V1ObjectMeta();
      metadata.setNamespace(nameSpace);
      metadata.setName(name);
      V1JobSpec v1JobSpec = v1beta1CronJob.getSpec().getJobTemplate().getSpec();
      v1Job.setApiVersion(ApiVersionEnum.BATCH_V1.getApiVersionType());
      v1Job.setKind(KindEnum.JOB.getKind());
      v1Job.setMetadata(metadata);
      v1Job.setSpec(v1JobSpec);
      BatchV1Api batchV1Api = k8sApiService.getBatchV1Api();
      V1Job job = batchV1Api.createNamespacedJob(nameSpace, v1Job, CreateParam.pretty, CreateParam.dryRun, CreateParam.fieldManager);
      if (job != null) {
        return true;
      }
      
    } catch (ApiException e) {
      throw new ServiceException(ResultCode.QUERY_CRON_JOB_FAIL);
    }
    return false;
  }
  
  @Override
  public Boolean createCronJob(TAutoJob job) {
    QueryWrapper<TAutoYaml> queryWrapper = new QueryWrapper();
    queryWrapper.eq("type", K8sKind.CRON_JOB.value);
    TAutoYaml yaml = yamlService.getOne(queryWrapper);
    V1beta1CronJob newV1Job = K8sUtils.toObject(yaml.getYaml(), V1beta1CronJob.class);
    newV1Job.getMetadata().setNamespace(job.getModuleId());
    newV1Job.getMetadata().setName(job.getId());
    
    newV1Job.getSpec().setSchedule(job.getCronExpression());
    V1Container v1Container = newV1Job.getSpec().getJobTemplate().getSpec().getTemplate().getSpec().getContainers().get(0);
    List<String> args = new ArrayList<>();
    args.add(job.getId());
    v1Container.setArgs(args);
    //v1Container.setName();
    BatchV1beta1Api batchV1beta1Api = k8sApiService.getBatchV1beta1Api();
    V1beta1CronJob v1beta1CronJob = null;
    try {
      v1beta1CronJob = batchV1beta1Api.createNamespacedCronJob(job.getModuleId(), newV1Job, CreateParam.pretty, CreateParam.dryRun, CreateParam.fieldManager);
    } catch (ApiException e) {
      throw new ServiceException("创建cron失败！！！");
    }
    if (v1beta1CronJob != null) {
      return true;
    }
    return false;
  }
  
  @Override
  public Boolean cronJobToJob(TAutoJob autoJob) {
    V1beta1CronJob cronJob = readCronJob(autoJob.getModuleId(), autoJob.getId());
    V1Job v1Job = Jobs.cronJobToJob(cronJob, cronJob.getMetadata().getName() + "-" + RandomUtils.nextInt(500, 100000));
    if (v1Job != null) {
      BatchV1Api batchV1Api = k8sApiService.getBatchV1Api();
      V1Job job = null;
      try {
        job = batchV1Api.createNamespacedJob(cronJob.getMetadata().getNamespace(), v1Job,
            CreateParam.pretty, CreateParam.dryRun, CreateParam.fieldManager);
        if (job != null) {
          return true;
        }
      } catch (ApiException e) {
        throw new ServiceException("创建job失败！！！");
      }
      
    }
    return false;
  }
  
  @Override
  public Boolean suspendOrRecoverJob(TAutoJob autoJob) {
    BatchV1beta1Api batchV1Api = k8sApiService.getBatchV1beta1Api();
    V1beta1CronJob cronJob = readCronJob(autoJob.getModuleId(), autoJob.getId());
    if (cronJob == null) {
      throw new ServiceException("该任务没有找到！！！");
    }
    V1beta1CronJobSpec jobSpec = cronJob.getSpec();
    jobSpec.setSuspend(!jobSpec.getSuspend());
    try {
      batchV1Api.replaceNamespacedCronJob(cronJob.getMetadata().getName(), cronJob.getMetadata().getNamespace(), cronJob, ReplaceParam.pretty, ReplaceParam.dryRun, ReplaceParam.fieldManager);
    } catch (ApiException e) {
      throw new ServiceException("暂停job失败！！！");
    }
    return true;
  }
}
