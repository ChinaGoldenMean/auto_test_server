package com.auto.test.service.impl;

import com.auto.test.dao.TApiResultDao;
import com.auto.test.dao.TApiTestCaseResultDao;
import com.auto.test.entity.TApiCaseResult;
import com.auto.test.entity.TApiResult;
import com.auto.test.service.TAutoResultService;
import com.auto.test.service.TAutoTestCaseResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TAutoTestCaseResultServiceImpl extends ServiceImpl<TApiTestCaseResultDao, TApiCaseResult> implements TAutoTestCaseResultService {

}
