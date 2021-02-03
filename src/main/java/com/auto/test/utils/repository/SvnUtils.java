package com.auto.test.utils.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class SvnUtils {
  
  public static final Integer LATEST_REVERSION = -1;
  
  /**
   * 通过不同的协议初始化版本库
   */
  static {
    DAVRepositoryFactory.setup();
    SVNRepositoryFactoryImpl.setup();
    FSRepositoryFactory.setup();
  }
  
  /**
   * 验证登录svn
   */
  public static SVNRepository authSvn(String svnUrl, String username, String password) {
    SVNRepository svnRepository = null;
    try {
      svnRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
    } catch (SVNException e) {
      log.error(e.getErrorMessage().getMessage());
      return null;
    }
    ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
    svnRepository.setAuthenticationManager(authManager);
    return svnRepository;
  }
  
  public static SVNClientManager getManager(String username, String password) {
    ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
    DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
    SVNClientManager clientManager = SVNClientManager.newInstance(options);
    clientManager.setAuthenticationManager(authManager);
    return clientManager;
  }
  
  /**
   * 确定一个URL在SVN上是否存在 true 为存在  false 为不存在
   */
  public static boolean isURLExist(String svnUrl, String username, String password) throws SVNException {
    log.info("验证url是否存在: {}", svnUrl);
    SVNRepository svnRepository = authSvn(svnUrl, username, password);
    if (svnRepository != null) {
      SVNNodeKind nodeKind = svnRepository.checkPath("", -1); //遍历SVN,获取结点。
      return !nodeKind.equals(SVNNodeKind.NONE);
    }
    return false;
  }
  
  public static Long getUrlReversion(String svnUrl, String username, String password) throws SVNException {
    log.info("验证url是否存在: url {} username {} password {}", svnUrl, username, password);
    Long latestRevision = null;
    SVNRepository svnRepository = authSvn(svnUrl, username, password);
    if (svnRepository != null) {
      latestRevision = svnRepository.getLatestRevision();
    }
    return latestRevision;
  }
  
  /**
   * 更新svn代码路径
   *
   * @param clientManager    svn客户端管理
   * @param wcPath           当前workdir路径
   * @param updateToRevision 更新到哪个版本
   * @param depth            路径深度
   * @return 更新后的svn的版本
   */
  public static long update(SVNClientManager clientManager, File wcPath,
                            SVNRevision updateToRevision, SVNDepth depth) throws SVNException {
    //执行svn的操作
    long reversion = 0;
//        synchronized (SvnUtils.class) {
    if (wcPath.exists()) {
      log.info("执行svn up: " + wcPath.getAbsolutePath());
      SVNUpdateClient updateClient = clientManager.getUpdateClient();
      updateClient.setIgnoreExternals(false);
      reversion = updateClient.doUpdate(wcPath, updateToRevision, depth, false, false);
//            }
    }
    return reversion;
  }
  
  /**
   * 下拉svn代码到指定路径
   *
   * @param clientManager svn管理
   * @param url           svn地址
   * @param revision      版本
   * @param destPath      指定路径
   * @param depth         深度
   * @return 返回版本号
   */
  public static long checkout(SVNClientManager clientManager, SVNURL url, SVNRevision revision,
                              File destPath, SVNDepth depth) throws SVNException {
    log.info("checkout 代码:{}", url);
    SVNUpdateClient updateClient = clientManager.getUpdateClient();
    updateClient.setIgnoreExternals(true);
    return updateClient.doCheckout(url, destPath, revision, revision, depth, true);
  }
  
  public static boolean authSvnReversion(String svnUrl, String username, String password, Integer reversion, String path)
      throws SVNException {
    List<SVNLogEntry> entryList = new ArrayList<>();
    if (StringUtils.isNotBlank(svnUrl) && StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)
        && reversion != null && StringUtils.isNotBlank(password)) {
      SVNClientManager clientManager = getManager(username, password);
      SVNLogClient logClient = clientManager.getLogClient();
      logClient.doLog(SVNURL.parseURIEncoded(svnUrl), new String[]{""}, null, SVNRevision.create(reversion),
          SVNRevision.create(reversion), true, true, 5, new ISVNLogEntryHandler() {
            @Override
            public void handleLogEntry(SVNLogEntry svnLogEntry) throws SVNException {
              entryList.add(svnLogEntry);
            }
          });
    }
    return entryList.size() > 0;
  }
  
}
