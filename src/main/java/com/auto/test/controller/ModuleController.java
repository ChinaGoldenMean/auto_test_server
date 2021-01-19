package com.auto.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auto.test.entity.TAutoModule;
import com.auto.test.model.bo.base.JsonResult;
import com.auto.test.service.TAutoModuleService;
import com.auto.test.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * (TAutoModule)表控制层
 *
 * @author litiewang
 * @since 2020-12-18 15:15:46
 */
@RestController
@RequestMapping("remote")
public class ModuleController {
  /**
   * 服务对象
   */
  @Autowired
  private RestTemplate restTemplate;
  
  @PostMapping("module")
  public JsonResult<Object> module() {
    //8084是tomcat端口号+接口名+不加项目名
    String url = "http://134.96.252.42:8000/multiauth/K8s_monitor/getunregistermodule2/";
    
    HttpHeaders headers = new HttpHeaders();
    HttpMethod method = HttpMethod.POST;
    // 以表单的方式提交
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    //将请求头部和参数合成一个请求
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
    //执行HTTP请求，将返回的结构使用ResultVO类格式化
    ResponseEntity<Object> response = restTemplate.exchange(url, method, requestEntity, Object.class);
    return JsonResult.success(response.getBody());
  }
  
  @PostMapping("center")
  public JsonResult<JSONObject> center(String passid) {
    //8084是tomcat端口号+接口名+不加项目名
    String url = "http://134.96.252.42:8000/multiauth/K8s_monitor/getcenterlist/";
    JSONObject json = new JSONObject();
    json.put("passid", passid);
    
    String result = ResponseUtil.doPost(url, json.toJSONString());
    
    return JsonResult.success(JSONObject.parseObject(result));
//    HttpHeaders headers = new HttpHeaders();
//    HttpMethod method = HttpMethod.POST;
//    // 以表单的方式提交
//   headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//    //将请求头部和参数合成一个请求
//    MultiValueMap<String, String> stringMultiValueMap = new LinkedMultiValueMap<>();
//
//    stringMultiValueMap.add("passid", "3");
//    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(stringMultiValueMap, headers);
//    //执行HTTP请求，将返回的结构使用ResultVO类格式化
//    ResponseEntity<Object> response = restTemplate.exchange(url, method, requestEntity, Object.class);
//    return JsonResult.success(response.getBody());
  }
  
  @GetMapping("paas")
  public JsonResult<Object> paas() {
    //8084是tomcat端口号+接口名+不加项目名
    String url = "http://134.96.252.42:8000/multiauth/K8s_monitor/getpaasid2/";
    
    return JsonResult.success(restTemplate.getForObject(url, Object.class));
  }
  
}