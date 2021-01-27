package com.auto.test.myTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.apiggs.Apiggs;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    ValidatableResponse resp = get("/v2/api-docs").then().body("basePath", equalTo("/api"));
    resp.statusCode(200);
  }
  @Test
  public void testPom() {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Apiggs apiggs = new Apiggs();
    System.out.println(apiggs.toString());
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