package com.auto.test.controller.k8s;

import com.auto.test.k8s.dto.SearchParamDTO;
import com.auto.test.k8s.model.Namespace;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.service.common.EventsService;
import com.auto.test.service.common.K8sApiService;
import com.auto.test.service.common.NamespaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "namespace")
@Api(tags = "命名空间管理")
@Slf4j
public class K8sManageNamespaceController {
  
  @Autowired
  private NamespaceService namespaceResource;
  
  @Autowired
  EventsService eventsService;
  
  @Autowired
  K8sApiService apiService;
  
  @GetMapping(value = "/list")
  @ApiOperation("获取所有的namespace列表")
  public JsonResult<Page<List<Namespace>>> getNamespaceList(SearchParamDTO paramVo) {
    log.debug("SearchParamDTO: {} ", paramVo);
    paramVo.validateParams();
    Page<List<Namespace>> namespacePageList = namespaceResource.getNamespaceList(paramVo);
    return JsonResult.success(namespacePageList);
  }
  
}
