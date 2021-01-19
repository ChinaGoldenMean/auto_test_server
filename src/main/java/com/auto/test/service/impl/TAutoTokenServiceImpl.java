package com.auto.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auto.test.common.dto.LoginUser;
import com.auto.test.common.dto.Token;
import com.auto.test.entity.TAutoToken;
import com.auto.test.dao.TAutoTokenDao;
import com.auto.test.service.TAutoTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

/**
 * (TAutoToken)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-18 15:28:39
 */
@Service
@Slf4j
public class TAutoTokenServiceImpl implements TAutoTokenService {
  @Autowired
  private TAutoTokenDao tAutoTokenDao;
  
  /**
   * token过期秒数
   */
  @Value("${token.expire.seconds}")
  private Integer expireSeconds;
  /**
   * 私钥
   */
  @Value("${token.jwtSecret}")
  private String jwtSecret;
  
  private static Key KEY = null;
  private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";
  
  @Override
  public Token saveToken(LoginUser loginUser) {
    loginUser.setToken(UUID.randomUUID().toString());
    loginUser.setLoginTime(System.currentTimeMillis());
    loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
    
    TAutoToken model = new TAutoToken();
    model.setId(loginUser.getToken());
    model.setCreateTime(new Date());
    model.setUpdateTime(new Date());
    model.setExpireTime(new Date(loginUser.getExpireTime()));
    model.setVal(JSONObject.toJSONString(loginUser));
    
    tAutoTokenDao.insert(model);
    // 登陆日志
    // logService.save(loginUser.getId(), "登陆", true, null);
    
    String jwtToken = createJWTToken(loginUser);
    
    return new Token(jwtToken, loginUser.getLoginTime());
  }
  
  /**
   * 生成jwt
   *
   * @param loginUser
   * @return
   */
  private String createJWTToken(LoginUser loginUser) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(LOGIN_USER_KEY, loginUser.getToken());// 放入一个随机字符串，通过该串可找到登陆用户
    
    String jwtToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance())
        .compact();
    
    return jwtToken;
  }
  
  @Override
  public void refresh(LoginUser loginUser) {
    loginUser.setLoginTime(System.currentTimeMillis());
    loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
    
    TAutoToken model = tAutoTokenDao.queryById(loginUser.getToken());
    model.setUpdateTime(new Date());
    model.setExpireTime(new Date(loginUser.getExpireTime()));
    model.setVal(JSONObject.toJSONString(loginUser));
    
    tAutoTokenDao.update(model);
  }
  
  @Override
  public LoginUser getLoginUser(String jwtToken) {
    String uuid = getUUIDFromJWT(jwtToken);
    if (uuid != null) {
      TAutoToken model = tAutoTokenDao.queryById(uuid);
      return toLoginUser(model);
    }
    
    return null;
  }
  
  @Override
  public boolean deleteToken(String jwtToken) {
    String uuid = getUUIDFromJWT(jwtToken);
    if (uuid != null) {
      TAutoToken model = tAutoTokenDao.queryById(uuid);
      LoginUser loginUser = toLoginUser(model);
      if (loginUser != null) {
        tAutoTokenDao.deleteById(uuid);
        //    logService.save(loginUser.getId(), "退出", true, null);
        
        return true;
      }
    }
    
    return false;
  }
  
  private LoginUser toLoginUser(TAutoToken model) {
    if (model == null) {
      return null;
    }
    
    // 校验是否已过期
    if (model.getExpireTime().getTime() > System.currentTimeMillis()) {
      return JSONObject.parseObject(model.getVal(), LoginUser.class);
    }
    
    return null;
  }
  
  private Key getKeyInstance() {
    if (KEY == null) {
      synchronized (TAutoTokenService.class) {
        if (KEY == null) {// 双重锁
          byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
          KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        }
      }
    }
    
    return KEY;
  }
  
  private String getUUIDFromJWT(String jwt) {
    if ("null".equals(jwt) || StringUtils.isBlank(jwt)) {
      return null;
    }
    
    Map<String, Object> jwtClaims = null;
    try {
      jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
      return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
    } catch (ExpiredJwtException e) {
      log.error("{}已过期", jwt);
    } catch (Exception e) {
      log.error("{}", e);
    }
    
    return null;
  }
  
  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  @Override
  public TAutoToken queryById(String id) {
    return this.tAutoTokenDao.queryById(id);
  }
  
  /**
   * 查询多条数据
   *
   * @param offset 查询起始位置
   * @param limit  查询条数
   * @return 对象列表
   */
  @Override
  public List<TAutoToken> queryAllByLimit(int offset, int limit) {
    return this.tAutoTokenDao.queryAllByLimit(offset, limit);
  }
  
  /**
   * 新增数据
   *
   * @param tAutoToken 实例对象
   * @return 实例对象
   */
  @Override
  public TAutoToken insert(TAutoToken tAutoToken) {
    this.tAutoTokenDao.insert(tAutoToken);
    return tAutoToken;
  }
  
  /**
   * 修改数据
   *
   * @param tAutoToken 实例对象
   * @return 实例对象
   */
  @Override
  public TAutoToken update(TAutoToken tAutoToken) {
    this.tAutoTokenDao.update(tAutoToken);
    return this.queryById(tAutoToken.getId());
  }
  
  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  @Override
  public boolean deleteById(String id) {
    return this.tAutoTokenDao.deleteById(id) > 0;
  }
  
}