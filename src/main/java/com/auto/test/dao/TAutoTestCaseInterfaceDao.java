package com.auto.test.dao;

import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoTestcase;
import com.auto.test.model.dto.TestcaseApiDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * (TAutoTestCaseInterfaceDao)表数据库访问层
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
public interface TAutoTestCaseInterfaceDao extends BaseMapper<TAutoTestcase> {
  TestcaseApiDto selectDtoById(@Param("id") String id);
  
}