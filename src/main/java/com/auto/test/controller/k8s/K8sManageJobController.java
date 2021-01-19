package com.auto.test.controller.k8s;

import com.auto.test.common.dto.ResponseInfo;
import com.auto.test.k8s.dto.SearchParamDTO;
import com.auto.test.k8s.dto.YamlBaseDTO;
import com.auto.test.k8s.model.Job;
import com.auto.test.k8s.vo.JobVo;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.service.common.JobService;
import com.auto.test.service.common.K8sService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "job")
@Api(tags = "任务调度管理")
@Slf4j
public class K8sManageJobController {
  @Autowired
  JobService jobService;
  @Autowired
  K8sService k8sService;
  
  @GetMapping(value = "list")
  @ApiOperation("获取所有的任务调度列表")
  public JsonResult<Page<JobVo>> listJob(SearchParamDTO paramVo) {
    log.debug("SearchParamDTO: {} ", paramVo);
    paramVo.validateParams();
    Page<JobVo> list =
        jobService.listJob(paramVo);
    return JsonResult.success(list);
  }

//  @GetMapping(value = "detail")
//  @ApiOperation("读取任务")
//  @ApiImplicitParams({
//      @ApiImplicitParam(name = "nameSpace", value = "命名空间", required = true, paramType = "query", dataType = "String"),
//      @ApiImplicitParam(name = "name", value = "任务名称", required = true, paramType = "query", dataType = "String")
//  })
//  public JsonResult<Job> getJobInfo(@RequestParam("nameSpace") String nameSpace, @RequestParam("name") String name) {
//    log.debug("nameSpace name: {} ", nameSpace, name);
//
//    Job job =
//        jobService.readJob(nameSpace, name);
//
//    return JsonResult.success(job);
//  }
  
  @DeleteMapping(value = "delete")
  @ApiOperation("删除任务")
  public JsonResult<Boolean> deleteResourceInfo(String nameSpace, String jobName) {
    log.debug("nameSpace: {} ", nameSpace, jobName);
    return JsonResult.success(jobService.deleteJob(nameSpace, jobName));
  }
  
}
