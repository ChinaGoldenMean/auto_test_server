package com.auto.test.service;

import com.auto.test.entity.TAutoPermission;

import java.util.List;

/**
 * 权限管理表(TAutoPermission)表服务接口
 *
 * @author litiewang
 * @since 2020-12-18 15:10:34
 */
public interface TAutoPermissionService {
  
  List<TAutoPermission> getPermissionAll();
  
  List<TAutoPermission> getPermissionByUserId(String userId);
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TAutoPermission queryById(String id);
  
  /**
   * 查询多条数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  List<TAutoPermission> queryAllByLimit(int offset, int limit);
  
  /**
   * 新增数据
   *
   * @param tAutoPermission 实例对象
   * @return 实例对象
   */
  TAutoPermission insert(TAutoPermission tAutoPermission);
  
  /**
   * 修改数据
   *
   * @param tAutoPermission 实例对象
   * @return 实例对象
   */
  TAutoPermission update(TAutoPermission tAutoPermission);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  boolean deleteById(String id);
  
}