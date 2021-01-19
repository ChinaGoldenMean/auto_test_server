package com.auto.test.service.common;

import com.auto.test.k8s.dto.SearchParamDTO;
import com.auto.test.k8s.vo.PodVo;
import com.auto.test.model.bo.base.Page;
import io.kubernetes.client.openapi.models.*;
import okhttp3.Call;

import java.util.List;
import java.util.Map;

public interface PodService {
  /**
   * 根据查询对象获取容器组集合
   *
   * @param paramVo
   * @return
   */
  //Page<PodVo> listPod(SearchParamDTO paramVo);
  
  /**
   * 容器组数据封装
   *
   * @param list
   * @param paramVo
   * @return
   */
  // Page<PodVo> toPodPage(List<V1Pod> list, SearchParamDTO paramVo, String namespace);
  
  /**
   * 根据节点名称查询容器组集合
   *
   * @param nodeName
   * @return
   */
  V1PodList listV1PodByNode(String nodeName, Boolean isFilter);
  
  /**
   * 根据查询对象与节点名称获取容器组集合分页数据
   *
   * @param paramVo
   * @param nodeName
   * @return
   */
  //Page<PodVo> listPodByNode(SearchParamDTO paramVo, String nodeName);
  
  /**
   * 实时读取容器日志
   *
   * @param nameSpace
   * @param podName
   * @param containerName
   * @return
   */
  Call namespacedPodLogCall(String nameSpace, String podName, String containerName);
  
  Call namespacedPodLogCallByJobName(String nameSpace, String jobName);
  
  /**
   * 根据选择器查询容器组集合
   *
   * @param selector
   * @param nameSpace
   * @return
   */
  V1PodList listV1PodByLabelSelector(Map<String, String> selector, String nameSpace);
  
  /**
   * 根据对象元数据查询容器组集合
   *
   * @param v1ObjectMeta
   * @param isLabel
   * @param kind
   * @return
   */
  V1PodList listV1Pod(V1ObjectMeta v1ObjectMeta, boolean isLabel, String kind);
  
  /**
   * 根据标签查询容器组集合
   *
   * @param v1LabelSelector
   * @param namespace
   * @return
   */
  V1PodList listV1Pod(V1LabelSelector v1LabelSelector, String namespace, String ownerReferencesName);
  
  V1PodList listV1Pod(V1LabelSelector v1LabelSelector, String namespace);
  
  /**
   * 根据标签查询容器组集合
   *
   * @param selector
   * @param namespace
   * @return
   */
  V1PodList listV1Pod(Map<String, String> selector, String namespace);
  
  V1PodList filterOwnerReferences(V1PodList v1PodList, String ownerReferencesName);
  
  V1PodList filterOwnerReferences(V1PodList v1PodList, V1ReplicaSetList v1ReplicaSetList);
}
