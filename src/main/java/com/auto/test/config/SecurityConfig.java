package com.auto.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * spring security配置
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  @Autowired
  private AuthenticationSuccessHandler authenticationSuccessHandler;
  @Autowired
  private AuthenticationFailureHandler authenticationFailureHandler;
  @Autowired
  private LogoutSuccessHandler logoutSuccessHandler;
  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private TokenFilter tokenFilter;
  
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //http.csrf().disable();
    http
        // 关闭csrf保护功能（跨域访问）
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/api/**", "/websocket/**").permitAll();//访问API下无需登录认证权限
    // 基于token，所以不需要session
    //   http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    
    //  http.authorizeRequests()

//        .antMatchers("/", "/v2/api-docs/**", "/swagger-resources/**",
//            "/webjars/**", "/swagger-ui.html", "/v2/api-docs/**", "/monitor/druid/**",
//            "/static/**/*", "/js/**/*", "/userTest/**", "/hessian/**", "/profile/**",
//            "/springboot-admin/**", "/device/**", "/job/run/**")
//        .permitAll().anyRequest().authenticated();
//    http.formLogin().loginProcessingUrl("/login")
//        .successHandler(authenticationSuccessHandler)
//        .failureHandler(authenticationFailureHandler).and()
//        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
//    http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
//    // 解决不允许显示在iframe的问题
//    http.headers().frameOptions().disable();
//    http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }
  
}
