package com.auto.test.service;

import com.auto.test.common.dto.LoginUser;
import com.auto.test.common.dto.Token;
import com.auto.test.entity.TAutoToken;

import java.util.List;

/**
 * (TAutoToken)表服务接口
 *
 * @author litiewang
 * @since 2020-12-18 15:28:39
 */
public interface TAutoTokenService {
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TAutoToken queryById(String id);
  
  /**
   * 查询多条数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  List<TAutoToken> queryAllByLimit(int offset, int limit);
  
  /**
   * 新增数据
   *
   * @param tAutoToken 实例对象
   * @return 实例对象
   */
  TAutoToken insert(TAutoToken tAutoToken);
  
  /**
   * 修改数据
   *
   * @param tAutoToken 实例对象
   * @return 实例对象
   */
  TAutoToken update(TAutoToken tAutoToken);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  boolean deleteById(String id);
  
  Token saveToken(LoginUser loginUser);
  
  void refresh(LoginUser loginUser);
  
  LoginUser getLoginUser(String token);
  
  boolean deleteToken(String token);
}