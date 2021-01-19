package com.auto.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TAutoToken)实体类
 *
 * @author litiewang
 * @since 2020-12-18 15:28:39
 */
@Data
@TableName("t_auto_token")
public class TAutoToken implements Serializable {
  private static final long serialVersionUID = -43624875271477309L;
  /**
   * token
   */
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  /**
   * LoginUser的json串
   */
  private String val;
  
  private Date expireTime;
  
  private Date createTime;
  
  private Date updateTime;
  
}