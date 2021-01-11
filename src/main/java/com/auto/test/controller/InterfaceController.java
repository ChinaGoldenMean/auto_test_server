package com.auto.test.controller;

import com.auto.test.common.dto.ErrorInfo;
import com.auto.test.common.dto.ResponseInfo;
import com.auto.test.common.exception.ServiceException;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TApiResult;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.InterfaceClassifyParam;
import com.auto.test.model.po.ApiParam;
import com.auto.test.service.TAutoInterfaceService;
import com.auto.test.service.request.RequestExecutorServer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * (TAutoInterface)表控制层
 *
 * @author makejava
 * @since 2020-12-21 15:50:39
 */
@Slf4j
@RestController
@RequestMapping("interface")
@Api(tags = "接口管理")
public class InterfaceController {
  /**
   * 服务对象
   */
  @Resource
  private TAutoInterfaceService interfaceService;
  @Resource
  private RequestExecutorServer requestExecutorServer;
  
  @PostMapping("debug")
  @ApiOperation(value = "调试api")
  public JsonResult<TApiResult> debugTApi(@RequestBody TAutoInterface api) {
    Map<String, Object> caseVars = new ConcurrentHashMap<>();
    List<ApiParam> params = new ArrayList<>();
   
      TApiResult tApiResult = requestExecutorServer.executeHttpRequest(api, null, caseVars, params);
      return JsonResult.success(tApiResult);
    
  }
  
  @PutMapping("update")
  @ApiOperation(value = "编辑")
  public JsonResult<Boolean> editTApi(@RequestBody TAutoInterface tApi) {
//        List<TApi> tApis = tApiService.findByNameAndProjectIdAndIdNot(tApi.getName(), tApi.getProjectId(), tApi.getId());
//        if (tApis.size() > 0) {
//            return new ResponseInfo(false, new ErrorInfo(520, "接口" + tApi.getName() + "已存在"));
//        }
    //  tApi.setUpdateBy(UserUtil.getLoginUser().getUsername());
    
    return JsonResult.success(interfaceService.saveOrUpdate(tApi));
  }
  
  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne")
  public JsonResult<TAutoInterface> selectOne(String id) {
    return JsonResult.success(interfaceService.getById(id));
  }
  
  @PostMapping("listByName")
  public JsonResult<Page<TAutoInterface>> listInterfaceClassify(@RequestBody InterfaceClassifyParam classifyParam) {
    QueryWrapper<TAutoInterface> wrapper = new QueryWrapper();
    wrapper.like("name", classifyParam.getName());
    IPage<TAutoInterface> iPage = interfaceService.page(classifyParam.getPage(), wrapper);
    return JsonResult.success(new Page(iPage));
  }
  
  @PostMapping("saveOrUpdate")
  public JsonResult<Boolean> saveOrUpdate(@RequestBody TAutoInterface classifyParam) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("name", classifyParam.getName());
    List<TAutoInterface> classifyList = interfaceService.list(queryWrapper);
    
    if (classifyList != null && classifyList.size() >= 1) {
      if (StringUtils.isEmpty(classifyParam.getId()) || !StringUtils.isEmpty(classifyParam.getId()) && !classifyParam.getId().equals(classifyList.get(0).getId())) {
        //新增,更新时不能存在.
        throw new ServiceException("接口名称不为重复");
      }
      
    }
    return JsonResult.success(interfaceService.saveOrUpdate(classifyParam));
  }
  
  @DeleteMapping("deleteById")
  public JsonResult<Boolean> delete(String id) {
    return JsonResult.success(interfaceService.removeById(id));
  }
  
  @PostMapping(value = "/swaggerImport/{{moduleId}}")
  @ApiOperation("批量导入接口")
  public JsonResult<Boolean> swaggerImport( @RequestParam("moduleId") String moduleId,String apiUrl) {
    
    return JsonResult.success(interfaceService.swaggerImport(apiUrl, moduleId));
  }
}