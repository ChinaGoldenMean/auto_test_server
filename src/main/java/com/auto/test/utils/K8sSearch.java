package com.auto.test.utils;

import com.auto.test.dao.TAutoManageEnvDao;
import com.auto.test.k8s.constant.K8sParam;
import com.auto.test.k8s.dto.SearchParamDTO;
import com.auto.test.k8s.model.Role;
import com.auto.test.k8s.vo.ObjectMetaVo;
import com.auto.test.service.common.K8sApiService;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Slf4j
public class K8sSearch {
  
  protected Long totalItem = 0L;
  public static final String CREATE_TIME_STAMP = "createTimeStamp";
  public static final String ORDER = "d";
  @Autowired
  K8sApiService k8sApiService;
  
  @Resource
  TAutoManageEnvDao manageEnvDao;
  
  @SneakyThrows
  public <T> List<T> pagingOrder(SearchParamDTO vo, List<T> list, Function<T, V1ObjectMeta> getV1ObjectMeta, String namespaces) {
    
    String filterName = vo.getFilterBy();
    // String sortBy = vo.getSortBy();
    OrderItem order = vo.getOrder();
    Long limit = vo.getItemsPerPage();
    Long skip = vo.getSkip();
    if (list == null || list.isEmpty()) {
      this.totalItem = 0L;
      return list;
    }
    filterByNamespaces(list, getV1ObjectMeta, namespaces);
    if (!StringUtils.isEmpty(filterName)) {
      list = list.stream().filter(
          t -> {
            return getV1ObjectMeta.apply(t).getName().contains(vo.getFilterBy());
          }
      ).collect(Collectors.toList());
    }
    
    this.totalItem = new Long(list.size());
    list = list.stream().skip(skip).limit(limit).sorted((t1, t2) -> {
      V1ObjectMeta om1 = getV1ObjectMeta.apply(t1);
      V1ObjectMeta om2 = getV1ObjectMeta.apply(t2);
      return sort(om1, om2, order);
    }).collect(Collectors.toList());
    
    return list;
  }
  
  public <T> List<T> filterByNamespaces(List<T> list, Function<T, V1ObjectMeta> getV1ObjectMeta, String namespaces) {
    if (k8sApiService.isNotProd()) {
      if (!StringUtils.isEmpty(namespaces) && namespaces.indexOf(K8sParam.NAMESPACE_ALL) < 0) {
        String[] namespacesArray = namespaces.split(",");
        List<String> namespacesList = new ArrayList<String>(Arrays.asList(namespacesArray));
        namespacesList.remove(0);
        
        list = list.stream().filter(
            t -> {
              return namespacesList.contains(getV1ObjectMeta.apply(t).getNamespace());
            }
        ).collect(Collectors.toList());
        
      }
    }
    
    return list;
  }
  
  public <T> List<T> filterByNamespaces(List<T> list, Function<T, V1ObjectMeta> getV1ObjectMeta, String namespaces, SearchParamDTO vo) {
    list = filterByNamespaces(list, getV1ObjectMeta, namespaces);
    List<String> modelNames = vo.getModelNames();
    if (modelNames != null && modelNames.size() > 0) {
      
      list = list.stream().filter(
          t -> {
            
            for (String modelName : modelNames) {
              String namespace = getV1ObjectMeta.apply(t).getNamespace();
              
              if (namespace.contains(modelName)) {
                return true;
              }
              
            }
            return false;
            //    return modelNames.contains(getV1ObjectMeta.apply(t).getNamespace());
            
          }
      ).collect(Collectors.toList());
    }
    
    return list;
  }
  
  public int sort(V1ObjectMeta om1, V1ObjectMeta om2, OrderItem order) {
    return sort(new ObjectMetaVo(om1), new ObjectMetaVo(om2), order);
  }
  
  public int sort(Role.RoleInfo om1, Role.RoleInfo om2, OrderItem order) {
    Date data1 = om1.getMeta_creationTimestamp();
    Date data2 = om2.getMeta_creationTimestamp();
    String name1 = om1.getMeta_name();
    String name2 = om2.getMeta_name();
    return baseSort(data1, data2, name1, name2, order);
  }
  
  public int baseSort(Date data1, Date data2, String name1, String name2, OrderItem order) {
    if (order != null) {
      
      if (CREATE_TIME_STAMP.equals(order.getColumn())) {
        if (order.isAsc()) {
          return data1.compareTo(data2);
        } else {
          return data2.compareTo(data1);
        }
        
      } else {
        if (order.isAsc()) {
          return name1.compareTo(name2);
        } else {
          return name2.compareTo(name1);
        }
        
      }
      
    } else {
      return data2.compareTo(data1);
    }
  }
  
  public int sort(ObjectMetaVo om1, ObjectMetaVo om2, OrderItem order) {
    Date data1 = om1.getCreationTimestamp();
    Date data2 = om2.getCreationTimestamp();
    String name1 = om1.getName();
    String name2 = om2.getName();
    return baseSort(data1, data2, name1, name2, order);
  }
}
