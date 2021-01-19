package com.auto.test.dao;

import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.entity.TAutoModule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * (TAutoModule)表数据库访问层
 *
 * @author litiewang
 * @since 2020-12-21 15:52:59
 */
public interface TAutoModuleDao extends BaseMapper<TAutoModule> {
  @Select("SELECT * FROM t_auto_module sp ,t_auto_user_module up WHERE sp.id = up.projectId AND up.userId = #{userId}")
  List<TAutoModule> selectAllByUserId(String userId);
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  TAutoModule queryById(String id);
  
  /**
   * 查询指定行数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  List<TAutoModule> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);
  
  /**
   * 通过实体作为筛选条件查询
   *
   * @param tAutoModule 实例对象
   * @return 对象列表
   */
  List<TAutoModule> queryAll(TAutoModule tAutoModule);
  
  /**
   * 新增数据
   *
   * @param tAutoModule 实例对象
   * @return 影响行数
   */
  int insert(TAutoModule tAutoModule);
  
  /**
   * 修改数据
   *
   * @param tAutoModule 实例对象
   * @return 影响行数
   */
  int update(TAutoModule tAutoModule);
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(String id);
  
}