package com.auto.test.service;

import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoTestcase;
import com.auto.test.model.dto.TApiCaseResultDto;
import com.auto.test.model.dto.TestcaseApiDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (TAutoInterfaceClassify)表服务接口
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
public interface TestCaseInterfaceService extends IService<TAutoTestcase> {
  void saveTestCase(TestcaseApiDto testcaseApi);
  
  TestcaseApiDto selectDtoById(String id);
  
  TApiCaseResultDto excCase(TestcaseApiDto testcaseApi);
}