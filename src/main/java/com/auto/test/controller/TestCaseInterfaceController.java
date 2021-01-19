package com.auto.test.controller;

import com.auto.test.entity.TAutoStepInterface;
import com.auto.test.entity.TAutoTestcase;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.model.bo.base.Page;
import com.auto.test.model.dto.InterfaceClassifyParam;
import com.auto.test.model.dto.TApiCaseResultDto;
import com.auto.test.model.dto.TestcaseApiDto;
import com.auto.test.service.TStepApiService;
import com.auto.test.service.TestCaseInterfaceService;
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
@RequestMapping("testCase")
@Api(tags = "测试用例管理")
public class TestCaseInterfaceController {
  /**
   * 服务对象
   */
  @Resource
  private TestCaseInterfaceService caseInterfaceService;
  @Resource
  private TStepApiService stepApiService;
  
  @PostMapping("debug")
  @ApiOperation(value = "调试测试用例")
  public JsonResult<TApiCaseResultDto> debugCaseApi(@RequestBody TestcaseApiDto testcaseApi) {
    TApiCaseResultDto apiCaseResultDto = caseInterfaceService.excCase(testcaseApi);
    return JsonResult.success(apiCaseResultDto);
  }
  
  @PostMapping("saveOrUpdate")
  @ApiOperation(value = "编辑用例")
  public JsonResult<String> editTApi(@RequestBody TestcaseApiDto testcaseApi) {
    
    caseInterfaceService.saveTestCase(testcaseApi);
    return JsonResult.success("成功");
  }
  
  @PutMapping("editStep")
  @ApiOperation(value = "编辑用例步骤")
  public JsonResult<Boolean> editStep(@RequestBody TAutoStepInterface stepApiDto) {
    return JsonResult.success(stepApiService.updateById(stepApiDto));
  }
  
  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne")
  public JsonResult<TestcaseApiDto> selectOne(@RequestParam(value = "id") String id) {
    return JsonResult.success(caseInterfaceService.selectDtoById(id));
  }
  
  @PostMapping("listByName")
  public JsonResult<Page<TAutoTestcase>> listInterfaceClassify(@RequestBody InterfaceClassifyParam classifyParam) {
    QueryWrapper<TAutoTestcase> wrapper = new QueryWrapper();
    wrapper.like("name", classifyParam.getName());
    IPage<TAutoTestcase> iPage = caseInterfaceService.page(classifyParam.getPage(), wrapper);
    return JsonResult.success(new Page(iPage));
  }
  
  @DeleteMapping("deleteById")
  public JsonResult<Boolean> delete(@RequestParam(value = "id") String id) {
    //删除步骤表
    stepApiService.removeByTestCaseId(id);
    //删除用例表
    return JsonResult.success(caseInterfaceService.removeById(id));
  }
  
  @DeleteMapping("/removeStep")
  @ApiOperation(value = "删除步骤")
  public JsonResult<Boolean> deleteStep(@RequestParam(value = "ids[]") List<String> ids) {
    
    return JsonResult.success(stepApiService.removeByIds(ids));
  }
}