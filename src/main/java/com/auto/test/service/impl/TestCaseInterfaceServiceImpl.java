package com.auto.test.service.impl;

import com.auto.test.dao.TAutoTestCaseInterfaceDao;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoTestcase;
import com.auto.test.entity.TApiResult;
import com.auto.test.model.dto.StepApiDto;
import com.auto.test.model.dto.TApiCaseResultDto;
import com.auto.test.model.dto.TestcaseApiDto;
import com.auto.test.service.TStepApiService;
import com.auto.test.service.TestCaseInterfaceService;
import com.auto.test.service.request.TApiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * (TAutoInterfaceClassify)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Service
public class TestCaseInterfaceServiceImpl extends ServiceImpl<TAutoTestCaseInterfaceDao, TAutoTestcase> implements TestCaseInterfaceService {
  @Resource
  private TStepApiService stepApiService;
  @Resource
  private TAutoTestCaseInterfaceDao testCaseInterfaceDao;
  @Resource
  private TApiService apiService;
  
  @Override
  public TApiCaseResultDto excCase(TestcaseApiDto testcaseApi) {
    Map<String, Object> gVars = new ConcurrentHashMap<>();
    
    //globalParamService.findByProjectIdAndTypeAndEnvId(testcaseApi.getProjectId(), 2, testcaseApi.getEnvId());
    Map<String, Object> caseVars = new ConcurrentHashMap<>();
    
    TApiCaseResultDto tApiCaseResult = new TApiCaseResultDto();
    tApiCaseResult.setCreateTime(new Date());
    tApiCaseResult.setCaseId(testcaseApi.getId());
    tApiCaseResult.setCaseName(testcaseApi.getName());
    tApiCaseResult.setStatus(0);
//    List<CaseVar> caseVarList = testcaseApi.getCaseVars();
//    if (caseVarList != null) {
//      for (CaseVar caseVar : caseVarList) {
//        caseVars.put(caseVar.getName(), caseVar.getValue());
//      }
//    }
    List<StepApiDto> testSteps = testcaseApi.getTestSteps();
    List<TApiResult> stepResults = new ArrayList();
    int success = 0;
    int failed = 0;
    int skipped = 0;
    if (testSteps == null) {
      return tApiCaseResult;
    }
    for (StepApiDto stepApiDto : testSteps) {
      if (stepApiDto.getStatus() == 0) {
        continue;
      }
      TAutoInterface tApi = stepApiDto.getApi();
      
      tApi.setReqAssert(stepApiDto.getReqAssert());
      
      TApiResult tApiResult = apiService.executeHttpRequest(tApi, gVars, caseVars, stepApiDto.getApiParams());
      if (tApiResult.getResultType().equals(1)) {
        success = success + 1;
      } else {
        failed = failed + 1;
        tApiCaseResult.setStatus(1);
      }
      tApiResult.setStepId(stepApiDto.getId());
      tApiResult.setStepName(stepApiDto.getName());
      stepResults.add(tApiResult);
    }
    
    tApiCaseResult.setTotal(testSteps.size());
    tApiCaseResult.setSuccess(success);
    tApiCaseResult.setFailed(failed);
    tApiCaseResult.setSkipped(skipped);
    tApiCaseResult.setSteps(stepResults);
    tApiCaseResult.setEndTime(new Date());
    return tApiCaseResult;
  }
  
  @Override
  public void saveTestCase(TestcaseApiDto testcaseApi) {
    saveOrUpdate(testcaseApi);
    List<StepApiDto> testSteps = testcaseApi.getTestSteps();
    if (testSteps != null && testSteps.size() > 0) {
      for (StepApiDto stepApiDto : testSteps) {
        stepApiDto.setTestcaseId(testcaseApi.getId());
      }
    }
    stepApiService.saveSteps(testSteps);
  }
  
  @Override
  public TestcaseApiDto selectDtoById(String id) {
    
    return testCaseInterfaceDao.selectDtoById(id);
  }
}