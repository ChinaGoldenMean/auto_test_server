package com.auto.test.service.impl;

import com.auto.test.entity.TAutoModule;
import com.auto.test.dao.TAutoModuleDao;
import com.auto.test.service.TAutoModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TAutoModule)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-18 15:15:46
 */
@Service
public class TAutoModuleServiceImpl implements TAutoModuleService {
  @Autowired
  private TAutoModuleDao tAutoModuleDao;
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  @Override
  public TAutoModule queryById(String id) {
    return this.tAutoModuleDao.queryById(id);
  }
  
  /**
   * 查询多条数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  @Override
  public List<TAutoModule> queryAllByLimit(int offset, int limit) {
    return this.tAutoModuleDao.queryAllByLimit(offset, limit);
  }
  
  /**
   * 新增数据
   *
   * @param tAutoModule 实例对象
   * @return 实例对象
   */
  @Override
  public TAutoModule insert(TAutoModule tAutoModule) {
    this.tAutoModuleDao.insert(tAutoModule);
    return tAutoModule;
  }
  
  /**
   * 修改数据
   *
   * @param tAutoModule 实例对象
   * @return 实例对象
   */
  @Override
  public TAutoModule update(TAutoModule tAutoModule) {
    this.tAutoModuleDao.update(tAutoModule);
    return this.queryById(tAutoModule.getId());
  }
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  @Override
  public boolean deleteById(String id) {
    return this.tAutoModuleDao.deleteById(id) > 0;
  }
}