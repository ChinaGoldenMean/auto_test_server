package com.auto.test.dao;

import com.auto.test.entity.TAutoModule;
import com.auto.test.entity.TAutoUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * (TAutoUser)表数据库访问层
 *
 * @author litiewang
 * @since 2020-12-18 15:14:30
 */
public interface TAutoUserDao extends BaseMapper<TAutoUser> {
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TAutoUser queryById(String id);
  
  /**
   * 通过实体作为筛选条件查询
   *
   * @param tAutoUser 实例对象
   * @return 对象列表
   */
  List<TAutoUser> queryAll(TAutoUser tAutoUser);
  
  @Select("select * from t_auto_user t where t.username = #{username}")
  TAutoUser getUser(String username);
  
  /**
   * 修改数据
   *
   * @param tAutoUser 实例对象
   * @return 影响行数
   */
  int update(TAutoUser tAutoUser);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(String id);
  
}