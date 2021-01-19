package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 权限管理表(TAutoPermission)实体类
 *
 * @author litiewang
 * @since 2020-12-18 15:10:34
 */
@Data
@TableName("t_auto_permission")
public class TAutoPermission implements Serializable {
  private static final long serialVersionUID = -69714205127754252L;
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  /**
   * 菜单名称
   */
  private String name;
  /**
   * icon
   */
  private String icon;
  /**
   * 中文描述
   */
  private String title;
  /**
   * url
   */
  private String url;
  /**
   * 内容
   */
  private String component;
  /**
   * 父id
   */
  private String parentId;
  /**
   * 操作类型
   */
  private String type;
  /**
   * 排序
   */
  private Integer orderby;
  
  private List<TAutoPermission> children;
  
}