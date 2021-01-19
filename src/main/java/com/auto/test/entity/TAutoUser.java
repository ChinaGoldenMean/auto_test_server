package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TAutoUser)实体类
 *
 * @author litiewang
 * @since 2020-12-18 15:14:30
 */
@Data
@TableName("t_auto_user")
public class TAutoUser implements Serializable {
  private static final long serialVersionUID = -19422675863459943L;
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  /**
   * 角色id
   */
  private String roleId;
  /**
   * 姓名
   */
  private String username;
  /**
   * 密码
   */
  private String password;
  /**
   * 昵称
   */
  private String nickname;
  /**
   * 备注
   */
  private String remark;
  /**
   * 邮箱
   */
  private String email;
  /**
   * 手机
   */
  private Integer mobile;
  /**
   * 是否激活
   */
  private String isActive;
  
  private Integer qq;
  
  private Date createTime;
  
  private Date updateTime;
  
}