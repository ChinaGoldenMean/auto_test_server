//package com.auto.test.controller;
//
//import com.auto.test.entity.TAutoUser;
//import com.auto.test.service.TAutoUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
///**
// * (TAutoUser)表控制层
// *
// * @author litiewang
// * @since 2020-12-18 15:14:30
// */
//@RestController
//@RequestMapping("tAutoUser")
//public class TAutoUserController {
//  /**
//   * 服务对象
//   */
//  @Autowired
//  private TAutoUserService tAutoUserService;
//
//  /**
//   * 通过主键查询单条数据
//   *
//   * @param id 主键
//   * @return 单条数据
//   */
//  @GetMapping("selectOne")
//  public TAutoUser selectOne(String id) {
//    return this.tAutoUserService.queryById(id);
//  }
//
//}