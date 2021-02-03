package com.auto.test.controller.k8s;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auto.test.common.exception.ServiceException;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.InterfaceClassifyParam;
import com.auto.test.service.TAutoInterfaceService;
import com.auto.test.service.common.K8sApiService;
import com.auto.test.service.common.K8sService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.kubernetes.client.Discovery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing ManageEnv.
 *
 * @author ctsi-biyi-generator
 */
@RestController
@RequestMapping("test")
@Api(tags = "测试专用")
@Slf4j
public class ProbeController {
  @Autowired
  K8sService k8sService;
  @Autowired
  K8sApiService k8sApiService;
  
  @Resource
  private TAutoInterfaceService interfaceService;
  @GetMapping("probe")
  @ApiOperation("探针")
  public JsonResult<String> probe() {
    return JsonResult.success("我还活着");
  }
  
  @GetMapping("url")
  @ApiOperation("url测试")
  public JsonResult<JSONObject> test(String url) {
    String result = k8sService.executeHttpGetBack(url);
    
    return JsonResult.success(JSON.parseObject(result));
  }
  
  @RequestMapping("test-urlencoded")
  @ApiImplicitParam(paramType = "form")
  public JsonResult<Page<TAutoInterface>> urlencoded(@RequestParam InterfaceClassifyParam classifyParam) {
    QueryWrapper<TAutoInterface> wrapper = new QueryWrapper();
    wrapper.like("name", classifyParam.getName());
    IPage<TAutoInterface> iPage = interfaceService.page(classifyParam.getPage(), wrapper);
    return JsonResult.success(new Page(iPage));
  }
  @PostMapping("test-form")
  @ApiImplicitParam(paramType = "form")
  public JsonResult<Boolean> saveOrUpdate(@RequestBody TAutoInterface classifyParam) {
    
    return JsonResult.success(true);
  }
}
