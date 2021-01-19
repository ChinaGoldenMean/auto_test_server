package com.auto.test.dao;

import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.entity.TAutoToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TAutoToken)表数据库访问层
 *
 * @author litiewang
 * @since 2020-12-21 15:53:01
 */
public interface TAutoTokenDao extends BaseMapper<TAutoToken> {
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TAutoToken queryById(String id);
  
  /**
   * 查询指定行数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  List<TAutoToken> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);
  
  /**
   * 通过实体作为筛选条件查询
   *
   * @param tAutoToken 实例对象
   * @return 对象列表
   */
  List<TAutoToken> queryAll(TAutoToken tAutoToken);
  
  /**
   * 新增数据
   *
   * @param tAutoToken 实例对象
   * @return 影响行数
   */
  int insert(TAutoToken tAutoToken);
  
  /**
   * 修改数据
   *
   * @param tAutoToken 实例对象
   * @return 影响行数
   */
  int update(TAutoToken tAutoToken);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(String id);
  
}