//package com.auto.test.controller;
//
//import com.auto.test.entity.TAutoPermission;
//import com.auto.test.service.TAutoPermissionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
///**
// * 权限管理表(TAutoPermission)表控制层
// *
// * @author litiewang
// * @since 2020-12-18 15:10:36
// */
//@RestController
//@RequestMapping("tAutoPermission")
//public class  PermissionController {
//  /**
//   * 服务对象
//   */
//  @Autowired
//  private TAutoPermissionService tAutoPermissionService;
//
//  /**
//   * 通过主键查询单条数据
//   *
//   * @param id 主键
//   * @return 单条数据
//   */
//  @GetMapping("selectOne")
//  public TAutoPermission selectOne(String id) {
//    return this.tAutoPermissionService.queryById(id);
//  }
//
//}