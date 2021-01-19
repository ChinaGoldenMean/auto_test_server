package com.auto.test.service.common.impl;

import com.auto.test.entity.TAutoYaml;
import com.auto.test.k8s.constant.K8sConstant;
import com.auto.test.k8s.constant.K8sParam.ListParam;
import com.auto.test.common.exception.ServiceException;
import com.auto.test.k8s.dto.K8sYamlDTO;
import com.auto.test.k8s.dto.SearchParamDTO;
import com.auto.test.k8s.model.Event;
import com.auto.test.k8s.model.Namespace;
import com.auto.test.k8s.constant.K8sParam.*;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.bo.base.ResultCode;
import com.auto.test.model.constant.K8sKind;
import com.auto.test.service.TAutoYamlService;
import com.auto.test.service.common.EventsService;
import com.auto.test.service.common.K8sApiService;
import com.auto.test.service.common.NamespaceService;
import com.auto.test.utils.K8sSearch;
import com.auto.test.utils.K8sUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Event;
import io.kubernetes.client.openapi.models.V1Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NamespaceServiceImpl extends K8sSearch implements NamespaceService {
  
  @Autowired
  K8sApiService k8sService;
  @Autowired
  EventsService eventsService;
  
  @Autowired
  TAutoYamlService yamlService;
  
  @Override
  public Page<List<Namespace>> getNamespaceList(SearchParamDTO paramVo) {
    CoreV1Api coreV1Api = k8sService.getCoreV1Api();
    List<V1Namespace> items = null;
    
    try {
      items = coreV1Api.listNamespace(ListParam.pretty, ListParam.allowWatchBookmarks, ListParam._continue, ListParam.fieldSelector, ListParam.labelSelector, ListParam.limit, ListParam.resourceVersion, ListParam.timeoutSeconds, ListParam.watch).getItems();
      items = filterNamespace(items, paramVo);
    } catch (ApiException e) {
      throw new ServiceException(ResultCode.GET_NAMESPACE_FAIL);
    }
    List<V1Namespace> list = pagingOrder(paramVo, items, V1Namespace::getMetadata, null);
    List<Namespace> namespaceVos = new ArrayList<>();
    Long itemsPerPage = 0L;
    if (list != null && list.size() > 0) {
      list.stream().forEach(v1Namespace -> {
        Namespace vo = new Namespace(v1Namespace);
        namespaceVos.add(vo);
      });
      itemsPerPage = new Long(list.size());
    }
    
    Page<List<Namespace>> page = new Page(paramVo, namespaceVos, getTotalItem(), itemsPerPage);
    return page;
  }
  
  private List<V1Namespace> filterNamespace(List<V1Namespace> list, SearchParamDTO vo) {
    List<String> modelNames = vo.getModelNames();
    if (modelNames != null && modelNames.size() > 0) {
      
      list = list.stream().filter(
          t -> {
            for (String modelName : modelNames) {
              if (t.getMetadata().getName().contains(modelName)) {
                return true;
              }
              
            }
            return false;
            
          }
      ).collect(Collectors.toList());
    }
    
    return list;
    
  }
  
  @Override
  public V1Namespace nameSpacesByName(String name) {
    CoreV1Api coreV1Api = k8sService.getCoreV1Api();
    
    V1Namespace namespace = null;
    try {
      namespace = coreV1Api.readNamespace(name, ReadParam.pretty, ReadParam.exact, ReadParam.export);
    } catch (ApiException e) {
      
      if (e.getCode() != 404) {
        
        throw new ServiceException(ResultCode.GET_NAMESPACE_FAIL);
      }
    }
    return namespace;
  }
  
  @Override
  public Boolean createNamespace(String moduleId) {
    try {
      //如果命名空间为Null,则创建，否则直接创建cron任务
      if (!StringUtils.isEmpty(moduleId)) {
        QueryWrapper<TAutoYaml> queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", K8sKind.NAMESPACE.value);
        TAutoYaml yaml = yamlService.getOne(queryWrapper);
//        K8sYamlDTO k8SYamlDTO =  K8sUtils.transYaml2Vo( new File(K8sConstant.NAMESPACE_DEFAULT),moduleId);
        V1Namespace v1Namespace = K8sUtils.toObject(yaml.getYaml(), V1Namespace.class);
        v1Namespace.getMetadata().setName(moduleId);
        v1Namespace.setMetadata(v1Namespace.getMetadata());
        
        CoreV1Api coreV1Api = k8sService.getCoreV1Api();
        V1Namespace newV1Namespace = coreV1Api.createNamespace(
            v1Namespace, CreateParam.pretty, CreateParam.dryRun, CreateParam.fieldManager);
        if (newV1Namespace != null) {
          return true;
        }
      }
      
    } catch (ApiException e) {
      
      throw new ServiceException(ResultCode.RESOURCE_CREATE_FAIL);
    }
    
    return false;
  }
}
