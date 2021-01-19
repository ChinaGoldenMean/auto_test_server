//package com.auto.test.controller;
//
//import com.auto.test.entity.TRole;
//import com.auto.test.service.TRoleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
///**
// * (TRole)表控制层
// *
// * @author litiewang
// * @since 2020-12-18 16:51:00
// */
//@RestController
//@RequestMapping("tRole")
//public class TRoleController {
//    /**
//     * 服务对象
//     */
//    @Autowired
//    private TRoleService tRoleService;
//
//    /**
//     * 通过主键查询单条数据
//     *
//     * @param id 主键
//     * @return 单条数据
//     */
//    @GetMapping("selectOne")
//    public TRole selectOne(String id) {
//        return this.tRoleService.queryById(id);
//    }
//
//}