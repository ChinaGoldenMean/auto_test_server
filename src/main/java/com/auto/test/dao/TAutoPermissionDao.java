package com.auto.test.dao;

import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.entity.TAutoPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限管理表(TAutoPermission)表数据库访问层
 *
 * @author litiewang
 * @since 2020-12-21 15:53:00
 */
public interface TAutoPermissionDao extends BaseMapper<TAutoPermission> {
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TAutoPermission queryById(String id);
  
  /**
   * 查询指定行数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  List<TAutoPermission> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);
  
  @Select("select distinct p.* from t_auto_permission p inner join t_role_permission rp on p.id = rp.permission_id inner join t_auto_user ru on ru.role_id = rp.role_id where ru.user_id = #{userId} order by p.sort")
  List<TAutoPermission> listByUserId(String userId);
  
  /**
   * 通过实体作为筛选条件查询
   *
   * @param tAutoPermission 实例对象
   * @return 对象列表
   */
  List<TAutoPermission> queryAll(TAutoPermission tAutoPermission);
  
  /**
   * 新增数据
   *
   * @param tAutoPermission 实例对象
   * @return 影响行数
   */
  int insert(TAutoPermission tAutoPermission);
  
  /**
   * 修改数据
   *
   * @param tAutoPermission 实例对象
   * @return 影响行数
   */
  int update(TAutoPermission tAutoPermission);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(String id);
  
}