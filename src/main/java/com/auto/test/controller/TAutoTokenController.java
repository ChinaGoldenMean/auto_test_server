//package com.auto.test.controller;
//
//import com.auto.test.entity.TAutoToken;
//import com.auto.test.service.TAutoTokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
///**
// * (TAutoToken)表控制层
// *
// * @author litiewang
// * @since 2020-12-18 15:28:39
// */
//@RestController
//@RequestMapping("tAutoToken")
//public class TAutoTokenController {
//  /**
//   * 服务对象
//   */
//  @Autowired
//  private TAutoTokenService tAutoTokenService;
//
//  /**
//   * 通过主键查询单条数据
//   *
//   * @param id 主键
//   * @return 单条数据
//   */
//  @GetMapping("selectOne")
//  public TAutoToken selectOne(String id) {
//    return this.tAutoTokenService.queryById(id);
//  }
//
//}