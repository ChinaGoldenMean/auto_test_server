package com.auto.test.controller;

import com.auto.test.entity.TAutoInterfaceBug;
import com.auto.test.entity.TAutoJob;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.BugDto;
import com.auto.test.model.dto.JobDto;
import com.auto.test.model.dto.ParamList;
import com.auto.test.service.TAutoInterfaceBugService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TAutoInterface)表控制层
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Slf4j
@RestController
@RequestMapping("bug")
@Api(tags = "缺陷管理")
public class BugController {
  /**
   * 服务对象
   */
  @Resource
  private TAutoInterfaceBugService bugService;
  
  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne")
  public JsonResult<BugDto> selectOne(String id) {
    
    return JsonResult.success(bugService.getBugById(id));
  }
  
  @PostMapping("listByName")
  public JsonResult<Page<TAutoInterfaceBug>> listByName(@RequestBody ParamList paramList) {
    QueryWrapper<TAutoInterfaceBug> wrapper = new QueryWrapper();
    wrapper.like("name", paramList.getName());
    IPage<TAutoJob> iPage = bugService.page(paramList.getPage(), wrapper);
    return JsonResult.success(new Page(iPage));
  }
  
  @PostMapping("edit")
  public JsonResult<Boolean> saveOrUpdate(String remark, String id) {
    TAutoInterfaceBug bug = bugService.getById(id);
    bug.setRemark(remark);
    return JsonResult.success(bugService.updateById(bug));
  }
  
  @DeleteMapping("deleteById")
  public JsonResult<Boolean> delete(String id) {
    return JsonResult.success(bugService.removeById(id));
  }
  
}