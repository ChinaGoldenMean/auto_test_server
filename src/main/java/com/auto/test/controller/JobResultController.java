package com.auto.test.controller;

import com.auto.test.dao.TApiTestCaseResultDao;
import com.auto.test.dao.TAutoPlanApiResultDao;
import com.auto.test.entity.TAutoTestPlanApiResult;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.ParamList;
import com.auto.test.model.dto.TApiCaseResultDto;
import com.auto.test.model.dto.TApiPlanResultDto;
import com.auto.test.service.TAutoPlanApiResultService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ResultController)表控制层
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Slf4j
@RestController
@RequestMapping("job/result")
@Api(tags = "测试任务结果管理")
public class JobResultController {
  /**
   * 服务对象
   */
  @Resource
  private TAutoPlanApiResultService planApiResultService;
  @Resource
  private TAutoPlanApiResultDao  planApiResultDao;
  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne")
  @ApiOperation("任务结果id")
  public JsonResult<TApiPlanResultDto> selectOne(String id) {
  
    TApiPlanResultDto resultDto = planApiResultDao.selectOnePlanResult(id);
    return JsonResult.success(resultDto);
  }

  @PostMapping("listByCaseName")
  @ApiOperation("搜索条件name代表任务名称")
  public JsonResult<Page<TAutoTestPlanApiResult>> listByName(@RequestBody ParamList paramList) {
    QueryWrapper<TAutoTestPlanApiResult> wrapper = new QueryWrapper();
    wrapper.like("name", paramList.getName());
    IPage<TAutoTestPlanApiResult> iPage = planApiResultService.page(paramList.getPage(), wrapper);
    return JsonResult.success(new Page(iPage));
  }
//
//  @PostMapping("saveOrUpdate")
//  public JsonResult<Boolean> saveOrUpdate(@RequestBody JobDto job) {
//    QueryWrapper queryWrapper = new QueryWrapper();
//    queryWrapper.eq("name", job.getName());
//    List<TAutoInterface> classifyList = caseResultService.list(queryWrapper);
//
//
//    return JsonResult.success(caseResultService.saveOrUpdateJob(job));
//  }

  @DeleteMapping("deleteById")
  @ApiOperation("测试用例结果id")
  public JsonResult<Boolean> delete(String id) {
    return JsonResult.success(planApiResultService.removeById(id));
  }
}