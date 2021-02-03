package com.auto.test.utils.repository;

import com.auto.test.model.vo.RepositoryVo;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;

import java.io.File;

public class RepositoryUtils {
  public static void main(String[] args) {
    String svnUrl = "svn://134.96.231.131/quick_deploy_svn/back/ms-k8s-resource-manage-backend";
    RepositoryVo vo = new RepositoryVo();
    vo.setCodeUrl(svnUrl).setUsername("litiewang").setPassword("Ltw@1QAZ").setModuleId("test");
    RepositoryUtils.checkout(vo);
  }
  public static String getLocalPath(RepositoryVo repositoryVo){
    String codeUrl = repositoryVo.getCodeUrl();
    String[]  codeUrls= codeUrl.split("/");
    String localPath = repositoryVo.getBasePath()+repositoryVo.getModuleId()+"/";
    if(codeUrls!=null&&codeUrls.length>0){
      localPath+=codeUrls[codeUrls.length-1];
    }
  return localPath;
  }
  @SneakyThrows
  public  static void checkout(RepositoryVo repositoryVo){
    String username = repositoryVo.getUsername();
    String password = repositoryVo.getPassword();
    String codeUrl = repositoryVo.getCodeUrl();
    Boolean isExist= SvnUtils.isURLExist(repositoryVo.getCodeUrl(),username,password);
    String[]  codeUrls= codeUrl.split("/");
    String localPath = getLocalPath(repositoryVo);
    
    File file = new File(localPath);
    if(file.exists()){
      file.mkdirs();
    }
     if(isExist){
       SVNClientManager clientManager = SvnUtils.getManager(username, password);
       clientManager.createRepository(SVNURL
           .parseURIEncoded(codeUrl), true);
       SvnUtils.checkout(clientManager,
           SVNURL.parseURIEncoded(codeUrl),
           SVNRevision.create(SvnUtils.LATEST_REVERSION),
           file, SVNDepth.INFINITY);
     }else{
       gitCloneOrPull( repositoryVo,file);
     }
  }
  
  @SneakyThrows
  public static  void  gitCloneOrPull(RepositoryVo repositoryVo,File file){
    UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(repositoryVo.getUsername(), repositoryVo.getPassword());
    
    File[] files = file.listFiles();
    if(files!=null&&files.length>0){
      Git git = Git.open(file);
     git.fetch().setCredentialsProvider(provider).setTimeout(2000).call() ;
    
    }else{
      Git.cloneRepository().setURI(repositoryVo.getCodeUrl()).setDirectory(file).setCredentialsProvider(provider).setTimeout(1000).call().close();
    }
  }
  
}
