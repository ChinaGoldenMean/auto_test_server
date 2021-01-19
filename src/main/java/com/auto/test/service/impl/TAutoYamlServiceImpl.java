package com.auto.test.service.impl;

import com.auto.test.common.exception.ServiceException;
import com.auto.test.dao.TAutoJobDao;
import com.auto.test.dao.TAutoYamlDao;
import com.auto.test.dao.TJobSuiteApiDao;
import com.auto.test.entity.TAutoJob;
import com.auto.test.entity.TAutoYaml;
import com.auto.test.entity.TJobSuiteApi;
import com.auto.test.model.dto.JobDto;
import com.auto.test.service.TAutoJobService;
import com.auto.test.service.TAutoYamlService;
import com.auto.test.service.TJobSuiteApiService;
import com.auto.test.service.common.K8sApiService;
import com.auto.test.service.common.NamespaceService;
import com.auto.test.utils.CronUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.kubernetes.client.openapi.models.V1Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TAutoYamlService)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Service
public class TAutoYamlServiceImpl extends ServiceImpl<TAutoYamlDao, TAutoYaml> implements TAutoYamlService {

}