package com.auto.test.controller;

import com.auto.test.dao.TAutoJobDao;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoJob;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.JobDto;
import com.auto.test.model.dto.ParamList;
import com.auto.test.service.TAutoJobService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
@RequestMapping("cronJob")
@Api(tags = "任务代理管理")
public class JobController {
  /**
   * 服务对象
   */
  @Resource
  private TAutoJobService jobService;
  @Resource
  private TAutoJobDao tAutoJobDao;
//  @PutMapping("update")
//  @ApiOperation(value = "编辑")
//  public JsonResult<Boolean> editTApi(@RequestBody TAutoJob tApi) {
//
//    return JsonResult.success(jobService.saveOrUpdate(tApi));
//  }
//
  
  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne")
  public JsonResult<JobDto> selectOne(String id) {
    
    JobDto jobDto = tAutoJobDao.selectOneJob(id);
    return JsonResult.success(jobDto);
  }
  
  @PostMapping("listByName")
  public JsonResult<Page<TAutoJob>> listByName(@RequestBody ParamList paramList) {
    QueryWrapper<TAutoJob> wrapper = new QueryWrapper();
    wrapper.like("name", paramList.getName());
    IPage<TAutoJob> iPage = jobService.page(paramList.getPage(), wrapper);
    return JsonResult.success(new Page(iPage));
  }
  
  @PostMapping("saveOrUpdate")
  public JsonResult<Boolean> saveOrUpdate(@RequestBody JobDto job) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("name", job.getName());
    List<TAutoInterface> classifyList = jobService.list(queryWrapper);

//    if (classifyList != null && classifyList.size() >= 1) {
//      if (StringUtils.isEmpty(job.getId()) || !StringUtils.isEmpty(job.getId()) && !job.getId().equals(classifyList.get(0).getId())) {
//        //新增,更新时不能存在.
//        throw new ServiceException("任务名称不为重复");
//      }
//
//    }
    
    return JsonResult.success(jobService.saveOrUpdateJob(job));
  }
  
  @DeleteMapping("deleteById")
  public JsonResult<Boolean> delete(String id) {
    return JsonResult.success(jobService.deleteJob(id));
  }
  
  @PostMapping("/run")
  @ApiOperation("执行一次任务")
  public JsonResult<Boolean> run(String id) {
    return JsonResult.success(jobService.runJob(id));
  }
  
  @PostMapping("/suspendOrRecover")
  @ApiOperation("暂停或者恢复任务")
  public JsonResult<Boolean> suspendOrRecover(String id) {
    return JsonResult.success(jobService.suspendOrRecoverJob(id));
  }
  
  
  @PostMapping("/checkCron")
  @ApiOperation("检查cron表达式是否可用")
  public JsonResult<Boolean> checkCronExpressionIsValid(String cronExpression) {
    return JsonResult.success(jobService.checkCronExpressionIsValid(cronExpression));
  }
}