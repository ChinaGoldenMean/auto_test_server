package com.auto.test.dao;

import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoInterfaceClassify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * (TAutoInterfaceDao)表数据库访问层
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
public interface TAutoInterfaceDao extends BaseMapper<TAutoInterface> {
  
  TAutoInterface selectBySourceId(@Param("sourceId") String sourceId);
}