package com.auto.test.service.impl;

import com.auto.test.common.exception.ServiceException;
import com.auto.test.dao.TApiInterfaceBugDao;
import com.auto.test.entity.TApiResult;
import com.auto.test.entity.TAutoInterfaceBug;
import com.auto.test.model.dto.BugDto;
import com.auto.test.service.TAutoInterfaceBugService;
import com.auto.test.service.TAutoResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TAutoInterfaceBugServiceImpl extends ServiceImpl<TApiInterfaceBugDao, TAutoInterfaceBug> implements TAutoInterfaceBugService {
  @Resource
  TAutoResultService resultService;
  
  @Override
  public BugDto getBugById(String id) {
    TAutoInterfaceBug bug = getById(id);
    String resultId = bug.getResultId();
    if (resultId == null) {
      throw new ServiceException("没有找到测试日志数据 ！！！");
    }
    TApiResult result = resultService.getById(resultId);
    
    return new BugDto(bug, result);
  }
  
}
