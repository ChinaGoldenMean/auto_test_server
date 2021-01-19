package com.auto.test.controller.k8s;

import com.auto.test.dao.TAutoJobDao;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoJob;
import com.auto.test.entity.TAutoYaml;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.JobDto;
import com.auto.test.model.dto.ParamList;
import com.auto.test.service.TAutoYamlService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * REST controller for managing ManageEnv.
 *
 * @author ctsi-biyi-generator
 */
@RestController
@RequestMapping("yaml")
@Api(tags = "yaml配置文件管理")
@Slf4j
public class YamlController {
  /**
   * 服务对象
   */
  @Resource
  private TAutoYamlService yamlService;
  
  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne")
  public JsonResult<TAutoYaml> selectOne(String id) {
    
    TAutoYaml tAutoYaml = yamlService.getById(id);
    return JsonResult.success(tAutoYaml);
  }
  
  @PostMapping("listByType")
  public JsonResult<Page<TAutoYaml>> listByName(@RequestBody ParamList paramList) {
    QueryWrapper<TAutoYaml> wrapper = new QueryWrapper();
    wrapper.like("type", paramList.getName());
    IPage<TAutoYaml> iPage = yamlService.page(paramList.getPage(), wrapper);
    return JsonResult.success(new Page(iPage));
  }
  
  @PostMapping("saveOrUpdate")
  public JsonResult<Boolean> saveOrUpdate(@RequestBody TAutoYaml job) {

//    if (classifyList != null && classifyList.size() >= 1) {
//      if (StringUtils.isEmpty(job.getId()) || !StringUtils.isEmpty(job.getId()) && !job.getId().equals(classifyList.get(0).getId())) {
//        //新增,更新时不能存在.
//        throw new ServiceException("任务名称不为重复");
//      }
//
//    }
    
    return JsonResult.success(yamlService.saveOrUpdate(job));
  }
  
  @DeleteMapping("deleteById")
  public JsonResult<Boolean> delete(String id) {
    
    return JsonResult.success(yamlService.removeById(id));
  }
}
