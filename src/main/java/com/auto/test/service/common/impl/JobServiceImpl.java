package com.auto.test.service.common.impl;

import com.auto.test.k8s.constant.K8sParam.*;
import com.auto.test.entity.TAutoJob;
import com.auto.test.entity.TAutoYaml;
import com.auto.test.k8s.constant.K8sParam;
import com.auto.test.k8s.constant.K8sParam.ListParam;
import com.auto.test.common.exception.ServiceException;
import com.auto.test.k8s.constant.K8sParam.DeleteParam;
import com.auto.test.k8s.dto.SearchParamDTO;
import com.auto.test.k8s.model.Builder;
import com.auto.test.k8s.model.Event;
import com.auto.test.k8s.model.Job;
import com.auto.test.k8s.vo.JobVo;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.bo.base.ResultCode;
import com.auto.test.model.constant.K8sKind;
import com.auto.test.service.TAutoYamlService;
import com.auto.test.service.common.EventsService;
import com.auto.test.service.common.JobService;
import com.auto.test.service.common.K8sApiService;
import com.auto.test.service.common.PodService;
import com.auto.test.utils.CronUtils;
import com.auto.test.utils.K8sSearch;
import com.auto.test.utils.K8sUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.kubernetes.client.openapi.ApiException;
import com.auto.test.k8s.constant.K8sParam.ReadParam;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import okhttp3.Call;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class JobServiceImpl extends K8sSearch implements JobService {
  
  @Autowired
  TAutoYamlService yamlService;
  @Autowired
  PodService podService;
  @Autowired
  EventsService eventsService;
  @Autowired
  K8sApiService k8sService;
  
  @Override
  public Page<JobVo> listJob(SearchParamDTO paramVo) {
    BatchV1Api batchV1Api = k8sService.getBatchV1Api();
    String namespace = k8sService.getNamespace();
    
    List<V1Job> items;
    try {
      //  if (namespace.indexOf(K8sParam.NAMESPACE_ALL) == 0) {
      items = batchV1Api.listJobForAllNamespaces(ListParam.allowWatchBookmarks, ListParam._continue, ListParam.fieldSelector, ListParam.labelSelector, ListParam.limit, ListParam.pretty, ListParam.resourceVersion, ListParam.timeoutSeconds, ListParam.watch).getItems();
      items = filterByNamespaces(items, V1Job::getMetadata, namespace, paramVo);

//      } else {
//        items = batchV1Api.listNamespacedJob(namespace, ListParam.pretty, ListParam.allowWatchBookmarks, ListParam._continue, ListParam.fieldSelector, ListParam.labelSelector, ListParam.limit, ListParam.resourceVersion, ListParam.timeoutSeconds, ListParam.watch).getItems();
//      }
    } catch (ApiException e) {
      throw new ServiceException(ResultCode.QUERY_JOB_FAIL);
    }
    List<V1Job> list = pagingOrder(paramVo, items, V1Job::getMetadata, null);
    List<Job> jobs = generateListJob(list);
    if (jobs == null) {
      return null;
    }
    JobVo jobVo = new JobVo(jobs, list);
    Page<JobVo> page = new Page(paramVo, jobVo, getTotalItem(), new Long(jobs.size()));
    return page;
  }
  
  @Override
  public List<Job> generateListJob(List<V1Job> v1JobList) {
    List<Job> jobs = new ArrayList<>();
    if (v1JobList != null && v1JobList.size() > 0) {
      v1JobList.stream().forEach(v1Job -> {
        jobs.add(toJob(v1Job));
      });
    }
    return jobs;
  }
  
  @Override
  public Job toJob(V1Job v1Job) {
    V1PodList v1PodList = podService.listV1Pod(v1Job.getSpec().getSelector(), v1Job.getMetadata().getNamespace(), v1Job.getMetadata().getName());
    Set<Event> eventList = eventsService.generateEventSet(v1PodList);
    
    return new Job().initJob(v1Job, v1PodList, eventList);
  }
  
  @Override
  public List<Job> listJobByCronJob(String nameSpace, List<String> jobNames) {
    BatchV1Api batchV1Api = k8sService.getBatchV1Api();
    List<V1Job> v1Jobs = new ArrayList<>();
    if (jobNames != null && !jobNames.isEmpty()) {
      jobNames.stream().forEach(jobName -> {
        V1Job v1Job = null;
        try {
          v1Job = batchV1Api.readNamespacedJob(jobName, nameSpace, ReadParam.pretty, ReadParam.exact, ReadParam.export);
        } catch (ApiException e) {
          throw new ServiceException(ResultCode.QUERY_JOB_FAIL);
        }
        v1Jobs.add(v1Job);
      });
    }
    List<Job> jobs = generateListJob(v1Jobs);
    return jobs;
  }
  
  @Override
  public Job readJob(String nameSpace, String name) {
    
    BatchV1Api batchV1Api = k8sService.getBatchV1Api();
    V1Job v1Job;
    Job job = null;
    
    try {
      v1Job = batchV1Api.readNamespacedJob(name, nameSpace, ReadParam.pretty, ReadParam.exact, ReadParam.export);
    } catch (ApiException e) {
      throw new ServiceException(ResultCode.QUERY_JOB_ERROR);
    }
    //  V1PodList v1PodList = podService.listV1Pod(v1Job.getMetadata(), true, "Job");
    V1PodList v1PodList = podService.listV1Pod(v1Job.getSpec().getSelector(), v1Job.getMetadata().getNamespace(), v1Job.getMetadata().getName());
    Set<Event> eventSet = eventsService.generateEventSet(v1PodList);
    List<Event> podEventList = eventsService.listNamespacedEventByWarning(v1Job.getMetadata(), null);
    job = new Job(v1Job, v1PodList, eventSet, podEventList);
    
    return job;
  }
  
  @Override
  public Boolean deleteJob(String nameSpace, String name) {
    BatchV1Api batchV1Api = k8sService.getBatchV1Api();
    try {
      V1DeleteOptions v1DeleteOptions = new V1DeleteOptions();
      Long gracePeriodSeconds = 3L;
      v1DeleteOptions.setGracePeriodSeconds(gracePeriodSeconds);
      Call call = batchV1Api.deleteNamespacedJobCall(name,
          nameSpace, DeleteParam.pretty, DeleteParam.dryRun, DeleteParam.gracePeriodSeconds, DeleteParam.orphanDependents, DeleteParam.propagationPolicy, v1DeleteOptions, null);
      if (call != null) {
        call.execute();
        return true;
      }
    } catch (ApiException | IOException e) {
      throw new ServiceException(ResultCode.QUERY_JOB_ERROR);
    }
    return false;
  }
  
}
