package com.auto.test.controller;

import com.auto.test.common.exception.ServiceException;
import com.auto.test.dao.TAutoInterfaceClassifyDao;
import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.InterfaceClassifyParam;
import com.auto.test.service.TAutoInterfaceClassifyService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TAutoInterfaceClassify)表控制层
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@RestController
@RequestMapping("interfaceClassify")
@Api(tags = "分组管理")
public class InterfaceClassifyController {
  /**
   * 服务对象
   */
  @Resource
  private TAutoInterfaceClassifyService classifyService;
  
  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne")
  public JsonResult<TAutoInterfaceClassify> selectOne(String id) {
    return JsonResult.success(classifyService.getById(id));
  }
  
  @PostMapping("listByName")
  public JsonResult<Page<TAutoInterfaceClassify>> listInterfaceClassify(@RequestBody InterfaceClassifyParam classifyParam) {
// IPage<TAutoInterfaceClassify> iPage = classifyService.selectInterfaceClassifyPage(classifyParam);
    QueryWrapper<TAutoInterfaceClassify> wrapper = new QueryWrapper();
    wrapper.like("name", classifyParam.getName());
    IPage<TAutoInterfaceClassify> iPage = classifyService.page(classifyParam.getPage(), wrapper);
    // classifyService.listObjs()
    return JsonResult.success(new Page(iPage));
  }
  
  @PostMapping("saveOrUpdate")
  public JsonResult<Boolean> saveOrUpdate(@RequestBody TAutoInterfaceClassify classifyParam) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("name", classifyParam.getName());
    List<TAutoInterfaceClassify> classifyList = classifyService.list(queryWrapper);
    
    if (classifyList != null && classifyList.size() >= 1) {
      if (StringUtils.isEmpty(classifyParam.getId()) || !StringUtils.isEmpty(classifyParam.getId()) && !classifyParam.getId().equals(classifyList.get(0).getId())) {
        //新增,更新时不能存在.
        throw new ServiceException("分组名称不为重复");
      }
      
    }
    return JsonResult.success(classifyService.saveOrUpdate(classifyParam));
  }
  
  @DeleteMapping("deleteById")
  public JsonResult<Boolean> delete(String id) {
    return JsonResult.success(classifyService.removeById(id));
  }
}