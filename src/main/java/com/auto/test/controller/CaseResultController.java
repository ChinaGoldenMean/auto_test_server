package com.auto.test.controller;

import com.auto.test.dao.TApiTestCaseResultDao;
import com.auto.test.dao.TAutoJobDao;
import com.auto.test.entity.TApiCaseResult;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoJob;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.JobDto;
import com.auto.test.model.dto.ParamList;
import com.auto.test.model.dto.TApiCaseResultDto;
import com.auto.test.service.TAutoTestCaseResultService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ResultController)表控制层
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Slf4j
@RestController
@RequestMapping("case/result")
@Api(tags = "测试用例结果管理")
public class CaseResultController {
  /**
   * 服务对象
   */
  @Resource
  private TAutoTestCaseResultService caseResultService;
  
  @Resource
  private  TApiTestCaseResultDao caseResultDao;
  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne")
  @ApiOperation("测试用例结果id")
  public JsonResult<TApiCaseResultDto> selectOne(String id) {
  
    TApiCaseResultDto resultDto = caseResultDao.selectOneCaseResult(id);
    return JsonResult.success(resultDto);
  }

  @PostMapping("listByCaseName")
  @ApiOperation("搜索条件name代表测试用例名称")
  public JsonResult<Page<TApiCaseResult>> listByName(@RequestBody ParamList paramList) {
    QueryWrapper<TApiCaseResult> wrapper = new QueryWrapper();
    wrapper.like("case_name", paramList.getName());
    IPage<TApiCaseResult> iPage = caseResultService.page(paramList.getPage(), wrapper);
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
    return JsonResult.success(caseResultService.removeById(id));
  }
}