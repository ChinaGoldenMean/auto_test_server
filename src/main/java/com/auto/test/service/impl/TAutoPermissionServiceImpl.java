package com.auto.test.service.impl;

import com.auto.test.entity.TAutoPermission;
import com.auto.test.dao.TAutoPermissionDao;
import com.auto.test.service.TAutoPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理表(TAutoPermission)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-18 15:10:34
 */
@Service
public class TAutoPermissionServiceImpl implements TAutoPermissionService {
  @Autowired
  private TAutoPermissionDao tAutoPermissionDao;
  
  @Override
  public List<TAutoPermission> getPermissionAll() {
    final List<TAutoPermission> permissions = tAutoPermissionDao.queryAll(null);
    List<TAutoPermission> firstLevel = permissions.stream().filter(p -> p.getParentId().equals(0L)).collect(Collectors.toList());
    firstLevel.parallelStream().forEach(p -> {
      setChild(p, permissions);
    });
    return firstLevel;
  }
  
  @Override
  public List<TAutoPermission> getPermissionByUserId(String userId) {
    
    final List<TAutoPermission> permissions = tAutoPermissionDao.listByUserId(userId);
    List<TAutoPermission> firstLevel = permissions.stream().filter(p -> p.getParentId().equals(0L)).collect(Collectors.toList());
    firstLevel.parallelStream().forEach(p -> {
      setChild(p, permissions);
    });
    return firstLevel;
  }
  
  private void setChild(TAutoPermission p, List<TAutoPermission> permissions) {
    List<TAutoPermission> child = permissions.parallelStream().filter(a -> a.getParentId().equals(p.getId())).collect(Collectors.toList());
    p.setChildren(child);
    if (!CollectionUtils.isEmpty(child)) {
      child.parallelStream().forEach(c -> {
        //递归设置子元素，多级菜单支持
        setChild(c, permissions);
      });
    }
  }
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  @Override
  public TAutoPermission queryById(String id) {
    return this.tAutoPermissionDao.queryById(id);
  }
  
  /**
   * 查询多条数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  @Override
  public List<TAutoPermission> queryAllByLimit(int offset, int limit) {
    return this.tAutoPermissionDao.queryAllByLimit(offset, limit);
  }
  
  /**
   * 新增数据
   *
   * @param tAutoPermission 实例对象
   * @return 实例对象
   */
  @Override
  public TAutoPermission insert(TAutoPermission tAutoPermission) {
    this.tAutoPermissionDao.insert(tAutoPermission);
    return tAutoPermission;
  }
  
  /**
   * 修改数据
   *
   * @param tAutoPermission 实例对象
   * @return 实例对象
   */
  @Override
  public TAutoPermission update(TAutoPermission tAutoPermission) {
    this.tAutoPermissionDao.update(tAutoPermission);
    return this.queryById(tAutoPermission.getId());
  }
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  @Override
  public boolean deleteById(String id) {
    return this.tAutoPermissionDao.deleteById(id) > 0;
  }
}