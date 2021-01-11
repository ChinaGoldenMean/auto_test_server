package com.auto.test.myTest;

import cn.hutool.cron.pattern.CronPattern;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockTest {
  
  @Test
  public void test() {
    // 创建Mock对象，参数可以是类或者接口
    List<String> list = mock(List.class);
    
    //设置方法的预期返回值
    when(list.get(0)).thenReturn("zuozewei");
    when(list.get(1)).thenThrow(new RuntimeException("test exception"));
    
    String result = list.get(0);
    
    //验证方法调用
    Mockito.verify(list).get(0);
    
    //断言，list的第一个元素是否是"zuozwei"
    Assert.assertEquals(result, "zuozewei");
    
  }
  @Test
  public void test2() {
    Date date1 = new Date();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Date date2 = new Date();
    System.out.println(date1+":"+date2);
    System.out.println("相差"+(date2.getTime()-date1.getTime()));
    
  }
  
}

