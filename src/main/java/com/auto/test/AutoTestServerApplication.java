package com.auto.test;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

//@EnableAdminServer
@SpringBootApplication
@MapperScan("com.auto.test.dao")
//@EnableScheduling
@EnableSwagger2
//@ComponentScan("com.auto.test.config")
public class AutoTestServerApplication {
  
  public static void main(String[] args) {
  
    
    SpringApplication.run(AutoTestServerApplication.class, args);
  
  }
  
  //为了解决tomcat报错问题
  @Bean
  public ConfigurableServletWebServerFactory webServerFactory() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]"));
    return factory;
  }
  
  @Bean
  public MybatisPlusInterceptor paginationInterceptor() {
    MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
    //乐观锁
    //mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    //分页配置
    mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return mybatisPlusInterceptor;
  }
  
}

