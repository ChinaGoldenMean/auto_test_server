package com.auto.test.service.impl;

import com.auto.test.entity.TAutoUser;
import com.auto.test.dao.TAutoUserDao;
import com.auto.test.service.TAutoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TAutoUser)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-18 15:14:30
 */
@Service
public class TAutoUserServiceImpl implements TAutoUserService {
  @Autowired
  private TAutoUserDao tAutoUserDao;
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  @Override
  public TAutoUser queryById(String id) {
    return this.tAutoUserDao.queryById(id);
  }
  
  /**
   * 新增数据
   *
   * @param tAutoUser 实例对象
   * @return 实例对象
   */
  @Override
  public TAutoUser insert(TAutoUser tAutoUser) {
    this.tAutoUserDao.insert(tAutoUser);
    return tAutoUser;
  }
  
  /**
   * 修改数据
   *
   * @param tAutoUser 实例对象
   * @return 实例对象
   */
  @Override
  public TAutoUser update(TAutoUser tAutoUser) {
    this.tAutoUserDao.update(tAutoUser);
    return this.queryById(tAutoUser.getId());
  }
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  @Override
  public boolean deleteById(String id) {
    return this.tAutoUserDao.deleteById(id) > 0;
  }
}