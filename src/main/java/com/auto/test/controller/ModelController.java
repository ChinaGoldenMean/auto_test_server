package com.auto.test.controller;

import com.auto.test.entity.TAutoModel;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.ParamList;
import com.auto.test.service.TAutoModelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ModelController)表控制层
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Slf4j
@RestController
@RequestMapping("model")
@Api(tags = "数据模型管理")
public class ModelController {
  
  @Resource
  private TAutoModelService modelService;
  
  @PostMapping("listByName")
  public JsonResult<Page<TAutoModel>> listByName(@RequestBody ParamList paramList) {
    QueryWrapper<TAutoModel> wrapper = new QueryWrapper();
    wrapper.like("name", paramList.getName());
    IPage<TAutoModel> iPage = modelService.page(paramList.getPage(), wrapper);
    return JsonResult.success(new Page(iPage));
  }
  
  @GetMapping("selectOne")
  public JsonResult<TAutoModel> selectOne(String id) {
    
    TAutoModel model = modelService.getById(id);
    return JsonResult.success(model);
  }
  
  @PostMapping("saveOrUpdate")
  public JsonResult<Boolean> saveOrUpdate(@RequestBody TAutoModel job) {
    
    return JsonResult.success(modelService.saveOrUpdate(job));
  }
  
  @DeleteMapping("deleteById")
  public JsonResult<Boolean> delete(String id) {
    return JsonResult.success(modelService.removeById(id));
  }
}
