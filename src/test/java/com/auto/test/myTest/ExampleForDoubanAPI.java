package com.auto.test.myTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.alibaba.fastjson.JSON;
import com.auto.test.config.apiggs.CostomPostmanRender;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.model.postman.PostmanCollection;
import com.auto.test.model.postman.PostmanFolder;
import com.auto.test.model.postman.PostmanItem;
import com.auto.test.model.postman.PostmanReader;
import com.auto.test.model.vo.RepositoryVo;
import com.auto.test.utils.apitest.ApiUtil;

import com.auto.test.utils.repository.RepositoryUtils;
import com.github.apigcc.core.Apigcc;
import com.github.apigcc.core.Context;
import com.github.apigcc.core.common.postman.Folder;
import com.github.apigcc.core.common.postman.Postman;
import com.github.jsonzou.jmockdata.JMockData;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class ExampleForDoubanAPI {
  
  @Before
  public void before() {
    RestAssured.baseURI = "http://localhost:8890/api";
    RestAssured.port = 8890;
  }
  
  @Test
  //URL为http://api.douban.com/v2/book/1220562
  //判断Json中的返回信息title
  public void testGetBook() {
    RequestSpecification requestSpecification = ApiUtil.given();
  
  }
  public static String localRepoPath = "E:/project/idea/automationTest/temp/git/";
  
  public static String remoteRepoURI = "https://gitee.com/mrhj/form-generator-plugin.git";
  
  @SneakyThrows
  @Test
  public   void setupRepo() throws GitAPIException {
    
    Git git = Git.open(new File(localRepoPath));
    UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider("", "");
      File file = new File(localRepoPath);
    if(!file.exists()){
      file.mkdirs();
    }
    File[] files = file.listFiles();
    if(files!=null&&files.length>0){
      git.pull().setCredentialsProvider(provider).setTimeout(10).call();
    }else{
      Git.cloneRepository().setURI(remoteRepoURI).setDirectory(new File(localRepoPath)).setCredentialsProvider(provider).setTimeout(10).call();
    }
    
  }
  @SneakyThrows
  @Test
  public void postManTest(){
    String filePath ="E:\\Users\\litiewang\\Desktop\\AVA.postman_collection.json";
    PostmanReader reader = new PostmanReader();
  
    PostmanCollection postmanCollection = reader.readCollectionFile(filePath);
    log.info("Successfully parsed postman collection file: {}, Total parsed item count: {} ", filePath, JSON.toJSONString(postmanCollection));
  
    List<PostmanFolder>  postmanItems = postmanCollection.getItem();
    if(postmanItems!=null){
      postmanItems.stream().forEach(postmanItem -> {
        TAutoInterfaceClassify interfaceClassify = new TAutoInterfaceClassify();
        interfaceClassify.setName(postmanItem.getName());
      
        List<PostmanItem> items = postmanItem.getItem();
        if(items!=null&&items.size()>0){
          items.stream().forEach(postmanItemSubitem -> {
            List<PostmanItem>  item = postmanItemSubitem.getItem();
            if(item!=null&&item.size()>0){
              item.stream().forEach(itemSub -> {
                TAutoInterface autoInterface = new TAutoInterface(itemSub);
                autoInterface.setClassifyId(interfaceClassify.getId());
              });
            }else{
              TAutoInterface autoInterface = new TAutoInterface(postmanItemSubitem);
              log.info("autoInterface",JSON.toJSONString(autoInterface));
              autoInterface.setClassifyId(interfaceClassify.getId());
            }
           
          
          });
        }
      });
    }
    
  }
  @SneakyThrows
  @Test
  public void gitTest(){
    String svnUrl = "svn://134.96.231.131/quick_deploy_svn/back/ms-k8s-resource-manage-backend";
    RepositoryVo vo = new RepositoryVo();
    vo.setCodeUrl(svnUrl).setUsername("litiewang").setPassword("Ltw@1QAZ").setModuleId("test");
   RepositoryUtils.checkout(vo);
  }
  @SneakyThrows
  @Test
  public void testPom() {
  
    Context context = new Context();
    context.setId("ava");
    context.setName("AVA");
    context.addSource(Paths.get("E:/project/idea/deploy/fast_deploy/"));
    
    Apigcc apigcc = new Apigcc(context);
    apigcc.parse();
    apigcc.render();
    CostomPostmanRender costomPostmanTreeHandler = new CostomPostmanRender();
    
    Postman postman =costomPostmanTreeHandler.build(apigcc.getProject());
   
    List<TAutoInterface> list = new ArrayList<>();
    List<Folder>  postmanItems = postman.getItem();
    if(postmanItems!=null){
      postmanItems.stream().forEach(postmanItem -> {
        TAutoInterfaceClassify interfaceClassify = new TAutoInterfaceClassify();
        interfaceClassify.setName(postmanItem.getName());
        
        List<Folder> items = postmanItem.getItem();
        if(items!=null&&items.size()>0){
          items.stream().forEach(postmanItemSubitem -> {
            TAutoInterface autoInterface = new TAutoInterface(postmanItemSubitem);
            autoInterface.setClassifyId(interfaceClassify.getId());
            
          });
        }
      });
    }
    //log.info("postman:",JSONObject.toJSONString(postmanCollection));
    //  Apigcc cc = new Apigcc();
  }
//  @Test
//  public void testPom() {
//
//    Environment context = new Environment();
//    context.id("deploy");
//    context.title("deploy");
//    //context.name("AVA");
//    context.source(Paths.get("E:/project/idea/deploy/fast_deploy/"));
//
//    Apiggs apigcc = new Apiggs(context);
//     apigcc.lookup().build(new CostomPostmanTreeHandler());
//     CostomPostmanTreeHandler costomPostmanTreeHandler = new CostomPostmanTreeHandler();
//
//    Postman postman =costomPostmanTreeHandler.buildPostman(apigcc.getTree());
//   // IParser parser = ParserFactory.getParser(CollectionVersion.V2);
//    //   PostmanCollection postmanCollection = parser.parse("E:/project/idea/automationTest/auto_test_server/build/apiggs/ava.json");
//
//    List<TAutoInterface> list = new ArrayList<>();
//    List<Folder>  postmanItems = postman.getItem();
//    if(postmanItems!=null){
//      postmanItems.stream().forEach(postmanItem -> {
//        TAutoInterfaceClassify interfaceClassify = new TAutoInterfaceClassify();
//        interfaceClassify.setName(postmanItem.getName());
//
//        List<Item> items = postmanItem.getItem();
//        if(items!=null&&items.size()>0){
//          items.stream().forEach(postmanItemSubitem -> {
//            TAutoInterface autoInterface = new TAutoInterface();
//            autoInterface.setClassifyId(interfaceClassify.getId());
//          });
//        }
//
//      });
//    }
//    //log.info("postman:",JSONObject.toJSONString(postmanCollection));
//    //  Apigcc cc = new Apigcc();
//  }
  
  @Test
  public void testMock() {
    int intNum = JMockData.mock(int.class);
    int[] intArray = JMockData.mock(int[].class);
    Integer integer = JMockData.mock(Integer.class);
    Integer[] integerArray = JMockData.mock(Integer[].class);
//常用类型模拟
    BigDecimal bigDecimal = JMockData.mock(BigDecimal.class);
    BigInteger bigInteger = JMockData.mock(BigInteger.class);
    Date date = JMockData.mock(Date.class);
    String str = JMockData.mock(String.class);
    log.info("数据：",intNum,intArray,integer,integerArray,bigDecimal,bigInteger,date,str);
    T basicBean = JMockData.mock(T.class);
    
    //  Apigcc cc = new Apigcc();
  }
  @Test
  public void testSearchBook() {
    given().param("order.asc", "true").when().get("/api/namespace/list").then().body("message", equalTo("操作成功"));
  }
  
  @Test
  //解析JSON
  public void testParseJson() {
    ValidatableResponse resp = given().param("order.asc", "true").when().get("/api/namespace/list").then();
    //判断返回Json数据的title
    //判断二级属性rating.max的值
  //  resp.statusCode(200);
    ExtractableResponse response =resp.extract();
    System.out.println(response.body().asString());
    
    resp.body("status", equalTo(200));
    resp.body("result.currentPage", equalTo(1));
    //调用数组的方法判断数组的大小
    resp.body("result.data.size()", is(9));
    //判断数组第一个对象的值
    resp.body("result.data[0].meta_name", equalTo("auto-test"));
    //判断数组中是否有该元素resp.body("result.data", hasItems("meta_name",""auto"));
  }
  
  @After
  public void after() {
  }
  
}