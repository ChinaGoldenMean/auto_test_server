package com.auto.test.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import io.cronitor.client.CronitorClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  
  /**
   * 跨域支持
   *
   * @return
   */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowCredentials(true);
      }
    };
  }
  
  /**
   * 外部文件访问
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
     registry.addResourceHandler("/template/**")
      .addResourceLocations("classpath:/file_template/");
    // 解决swagger无法访问
    
    registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    // 解决swagger的js文件无法访问
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
  
  @Bean
  public CronitorClient newCronitorClient() {
    return new CronitorClient();
  }
}
