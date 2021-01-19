package com.auto.test.service.impl;

import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.dao.TAutoInterfaceClassifyDao;
import com.auto.test.model.dto.InterfaceClassifyParam;
import com.auto.test.service.TAutoInterfaceClassifyService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * (TAutoInterfaceClassify)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Service
public class TAutoInterfaceClassifyServiceImpl extends ServiceImpl<TAutoInterfaceClassifyDao, TAutoInterfaceClassify> implements TAutoInterfaceClassifyService {

}