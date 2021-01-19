package com.auto.test.service;

import com.auto.test.entity.TAutoModule;

import java.util.List;

/**
 * (TAutoModule)表服务接口
 *
 * @author litiewang
 * @since 2020-12-18 15:15:46
 */
public interface TAutoModuleService {
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TAutoModule queryById(String id);
  
  /**
   * 查询多条数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  List<TAutoModule> queryAllByLimit(int offset, int limit);
  
  /**
   * 新增数据
   *
   * @param tAutoModule 实例对象
   * @return 实例对象
   */
  TAutoModule insert(TAutoModule tAutoModule);
  
  /**
   * 修改数据
   *
   * @param tAutoModule 实例对象
   * @return 实例对象
   */
  TAutoModule update(TAutoModule tAutoModule);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  boolean deleteById(String id);
  
}