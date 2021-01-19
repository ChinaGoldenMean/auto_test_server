package com.auto.test.dao;

import com.auto.test.entity.TAutoInterfaceClassify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * (TAutoInterfaceClassify)表数据库访问层
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
public interface TAutoInterfaceClassifyDao extends BaseMapper<TAutoInterfaceClassify> {

}