package com.auto.test.dao;

import com.auto.test.entity.TAutoModule;
import com.auto.test.entity.TRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (TRole)表数据库访问层
 *
 * @author litiewang
 * @since 2020-12-18 16:51:00
 */
public interface TRoleDao extends BaseMapper<TRole> {
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TRole queryById(String id);
  
  /**
   * 查询指定行数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  List<TRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);
  
  /**
   * 通过实体作为筛选条件查询
   *
   * @param tRole 实例对象
   * @return 对象列表
   */
  List<TRole> queryAll(TRole tRole);
  
  /**
   * 新增数据
   *
   * @param tRole 实例对象
   * @return 影响行数
   */
  int insert(TRole tRole);
  
  /**
   * 修改数据
   *
   * @param tRole 实例对象
   * @return 影响行数
   */
  int update(TRole tRole);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(String id);
  
}