package com.auto.test.service;

import com.auto.test.entity.TAutoUser;

/**
 * (TAutoUser)表服务接口
 *
 * @author litiewang
 * @since 2020-12-18 15:14:30
 */
public interface TAutoUserService {
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TAutoUser queryById(String id);
  
  /**
   * 新增数据
   *
   * @param tAutoUser 实例对象
   * @return 实例对象
   */
  TAutoUser insert(TAutoUser tAutoUser);
  
  /**
   * 修改数据
   *
   * @param tAutoUser 实例对象
   * @return 实例对象
   */
  TAutoUser update(TAutoUser tAutoUser);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  boolean deleteById(String id);
  
}