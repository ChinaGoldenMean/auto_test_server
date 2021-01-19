package com.auto.test.service.impl;

import com.auto.test.entity.TRole;
import com.auto.test.dao.TRoleDao;
import com.auto.test.service.TRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TRole)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-18 16:51:00
 */
@Service
public class TRoleServiceImpl implements TRoleService {
  @Autowired
  private TRoleDao tRoleDao;
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  @Override
  public TRole queryById(String id) {
    return this.tRoleDao.queryById(id);
  }
  
  /**
   * 查询多条数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  @Override
  public List<TRole> queryAllByLimit(int offset, int limit) {
    return this.tRoleDao.queryAllByLimit(offset, limit);
  }
  
  /**
   * 新增数据
   *
   * @param tRole 实例对象
   * @return 实例对象
   */
  @Override
  public TRole insert(TRole tRole) {
    this.tRoleDao.insert(tRole);
    return tRole;
  }
  
  /**
   * 修改数据
   *
   * @param tRole 实例对象
   * @return 实例对象
   */
  @Override
  public TRole update(TRole tRole) {
    this.tRoleDao.update(tRole);
    return this.queryById(tRole.getId());
  }
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  @Override
  public boolean deleteById(String id) {
    return this.tRoleDao.deleteById(id) > 0;
  }
}