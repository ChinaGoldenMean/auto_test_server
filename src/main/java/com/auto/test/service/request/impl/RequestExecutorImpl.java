package com.auto.test.service.request.impl;

import com.auto.test.entity.TAutoInterface;
import io.restassured.http.Header;
import com.auto.test.entity.TApiResult;
import com.auto.test.model.po.*;
import com.auto.test.service.request.RequestExecutorServer;
import com.auto.test.service.request.TApiService;
import com.auto.test.utils.BeanUtils;
import com.auto.test.utils.MyStringUtils;
import com.auto.test.utils.StringUtilHelper;
import com.auto.test.utils.apitest.ApiUtil;
import com.jayway.jsonpath.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.ConnectException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.*;

@Slf4j
@Service
public class RequestExecutorImpl implements RequestExecutorServer {
  
  @Resource
  private TApiService apiService;
  
  @Override
  public TApiResult executeHttpRequest(TAutoInterface tApi, Map<String, Object> gVars, Map<String, Object> caseVars, List<ApiParam> params) {
    TApiResult tApiResult = new TApiResult();
    if (params != null) {
      for (ApiParam apiParam : params) {
        apiParam.setValue(MyStringUtils.replaceKeyFromMap(apiParam.getValue(), gVars, caseVars));
      }
    }
    
    tApiResult.setCreateTime(new Date());
    RequestSpecification requestSpecification = ApiUtil.given();
    
    applyHeaders(requestSpecification, tApi, gVars, caseVars, tApiResult, params);
    applyQueryParameters(requestSpecification, tApi, gVars, caseVars, tApiResult, params);
    log.info("开始请求执行接口");
    tApi.setDomain(MyStringUtils.replaceKeyFromMap(tApi.getDomain(), caseVars, gVars));
    tApiResult.setReqMethod(tApi.getMethod());
    if (!tApi.getMethod().equalsIgnoreCase("get")) {
      tApiResult.setReqBodyType(tApi.getReqBodyType());
      if (tApi.getReqBodyType() == null) {
      } else if (tApi.getReqBodyType().equals("formdata")) {
        applyFormParam(requestSpecification, tApi, gVars, caseVars, tApiResult, params);
      } else if (tApi.getReqBodyType().equals("raw")) {
        applyRawParam(requestSpecification, tApi, gVars, caseVars, tApiResult, params);
      }
    }
    Response response = null;
    if (!tApi.getPath().startsWith("/")) {
      tApi.setPath("/" + tApi.getPath());
    }
    String url = tApi.getDomain() + tApi.getPath();
    if (url.indexOf("?") != -1) {
      url = url.substring(0, url.indexOf("?"));
    }
    url = MyStringUtils.replaceKeyFromMap(url, gVars, caseVars);
    tApiResult.setReqUrl(url);
    try {
      switch (tApi.getMethod().toUpperCase()) {
        case "GET":
          response = requestSpecification.when().get(url);
          break;
        case "POST":
          response = requestSpecification.when().post(url);
          break;
        case "PATCH":
          response = requestSpecification.when().patch(url);
          break;
        case "DELETE":
          response = requestSpecification.when().delete(url);
          break;
        case "PUT":
          response = requestSpecification.when().put(url);
          break;
        case "OPTIONS":
          response = requestSpecification.when().options(url);
          break;
        case "HEAD":
          response = requestSpecification.when().head(url);
          break;
        default:
          tApiResult.setException(String.format("不支持这个请求 %s.", url));
      }
    } catch (Exception e) {
      tApiResult.setResultType(-1);
      if (e instanceof UnknownHostException) {
        tApiResult.setException("请求异常,URL[" + e.getMessage() + "]无法连接");
      } else if (e instanceof ConnectException) {
        tApiResult.setException("请求异常,URL[" + tApi.getDomain() + "]无法连接");
      } else {
        tApiResult.setException("请求异常：" + e.getMessage());
        e.printStackTrace();
      }
      log.info("请求异常:{}", e.getMessage());
      long time = new Date().getTime() - tApiResult.getCreateTime().getTime();
      tApiResult.setRspTime(time);
      return tApiResult;
    }
    tApiResult.setRspStatusCode(response.getStatusCode());
    tApiResult.setRspTime(response.getTime());
    tApiResult.setRspBodySize(response.getBody().asByteArray().length);
    List<WebHeader> listHeader = new ArrayList<>();
    response.getHeaders().asList().forEach(x -> {
      WebHeader header = new WebHeader();
      header.setKey(x.getName());
      header.setValue(x.getValue());
      listHeader.add(header);
    });
    tApiResult.setRspHeaders(listHeader);
    tApiResult.setRspBody(response.getBody().prettyPrint());
    tApiResult.setRspBodyType(StringUtilHelper.checkStringFormat(tApiResult.getRspBody()));
    Boolean handResult = handleAssert(tApi, caseVars, tApiResult); //断言
    //  handleExtract(tApi, caseVars, tApiResult); //提取参数
    if (handResult) {
      //响应断言成功
      tApiResult.setResultType(1);
    } else {
      //断言失败
      tApiResult.setResultType(0);
    }
    return tApiResult;
  }
  
  private void applyHeaders(RequestSpecification requestSpecification, TAutoInterface tApi, Map<String, Object> gVars, Map<String, Object> caseVars, TApiResult TApiResult, List<ApiParam> params) {
    List<WebHeader> headers = tApi.getReqHeader();
    if (headers != null && headers.size() > 0) {
      for (WebHeader header : headers) {
        ApiParam apiParam = null;
        if (params != null) {
          apiParam = params.stream().filter(item -> item.getKey().equals("%." + header.getKey())).findFirst().orElse(null);
        }
        if (apiParam == null) {
          header.setValue(MyStringUtils.replaceKeyFromMap(header.getValue(), gVars, caseVars));
        } else {
          header.setValue(MyStringUtils.replaceKeyFromMap(apiParam.getValue(), gVars, caseVars));
        }
        if (header.getKey().equalsIgnoreCase("cookie")) {
          String[] cookies = header.getValue().split(";");
          for (String cookie : cookies) {
            String[] c = cookie.split("=");
            if (c != null && c.length < 2) {
              requestSpecification.cookie(cookie);
            } else {
              requestSpecification.cookie(c[0], c[1]);
            }
          }
        } else if (header.getKey().equalsIgnoreCase("Content-Type")) {
          log.info("我进来了：{}", header.getValue());
          requestSpecification.contentType(header.getValue());
        } else {
          requestSpecification.header(new Header(header.getKey(), header.getValue()));
        }
      }
    }
    
    TApiResult.setReqHeaders(headers);
  }
  
  private void applyQueryParameters(RequestSpecification requestSpecification, TAutoInterface tApi, Map<String, Object> gVars, Map<String, Object> caseVars, TApiResult tApiResult, List<ApiParam> params) {
    List<Query> reqQuerys = tApi.getReqQuery();
    try {
      for (Query reqQuery : reqQuerys) {
        ApiParam apiParam = null;
        if (params != null) {
          apiParam = params.stream().filter(item -> item.getKey().equals("#." + reqQuery.getKey())).findFirst().orElse(null);
        }
        if (apiParam == null) {
          reqQuery.setValue(MyStringUtils.replaceKeyFromMap(reqQuery.getValue().toString(), gVars, caseVars));
        } else {
          reqQuery.setValue(MyStringUtils.replaceKeyFromMap(apiParam.getValue(), gVars, caseVars));
        }
        requestSpecification.queryParam(reqQuery.getKey(), URLDecoder.decode(reqQuery.getValue().toString()));
      }
      tApiResult.setReqQuery(tApi.getReqQuery());
    } catch (Exception e) {
      tApiResult.setException("解析parameters失败：" + e.getMessage());
    }
  }
  
  private void applyFormParam(RequestSpecification requestSpecification, TAutoInterface tApi, Map<String, Object> gVars, Map<String, Object> caseVars, TApiResult TApiResult, List<ApiParam> params) {
    try {
      List<BodyData> reqBodyDatas = tApi.getReqBodyData();
      Map<String, Integer> formDataKeyNo = new HashMap();
      for (BodyData reqBodyData : reqBodyDatas) {
        //每个key计数器
        Integer integer = formDataKeyNo.get(reqBodyData.getKey());
        if (integer == null) {
          formDataKeyNo.put(reqBodyData.getKey(), 0);
        } else {
          formDataKeyNo.put(reqBodyData.getKey(), integer + 1);
        }
        
        String realKey = "$[" + formDataKeyNo.get(reqBodyData.getKey()) + "]." + reqBodyData.getKey();
        ApiParam apiParam = null;
        if (params != null) {
          apiParam = params.stream()
              .filter(item -> {
                String paramKey = item.getKey();
                if (paramKey.startsWith("$.")) {
                  paramKey = paramKey.replaceFirst("\\$\\.", "\\$[0].");
                }
                return paramKey.equals(realKey);
              }).findFirst().orElse(null);
        }
        if (apiParam == null) {
          reqBodyData.setValue(MyStringUtils.replaceKeyFromMap(reqBodyData.getValue().toString(), gVars, caseVars));
        } else {
          reqBodyData.setValue(MyStringUtils.replaceKeyFromMap(apiParam.getValue(), gVars, caseVars));
        }
        
        requestSpecification.formParam(reqBodyData.getKey(), reqBodyData.getValue());
      }
      TApiResult.setReqBodyData(tApi.getReqBodyData());
    } catch (Exception e) {
      TApiResult.setException("解析表单数据失败：" + e.getMessage());
    }
  }
  
  private void applyRawParam(RequestSpecification requestSpecification, TAutoInterface tApi, Map<String, Object> gVars, Map<String, Object> caseVars, TApiResult tApiResult, List<ApiParam> bParams) {
    tApi.setReqBodyJson(MyStringUtils.replaceKeyFromMap(tApi.getReqBodyJson(), gVars, caseVars));
    tApi.setReqBodyJson(MyStringUtils.changeString(tApi.getReqBodyJson(), bParams));
    tApiResult.setReqBodyJson(tApi.getReqBodyJson());
    requestSpecification.body(tApi.getReqBodyJson());
  }
  
  //批量断言
  private Boolean handleAssert(TAutoInterface tApi, Map<String, Object> caseVars, TApiResult TApiResult) {
    boolean flag = true;
    List<AssertResult> assertResults = new ArrayList();
    List<Assert> reqAssert = tApi.getReqAssert();
    for (Assert iassert : reqAssert) {
      AssertResult assertResult = assertion(iassert, caseVars, TApiResult);
      if (!assertResult.getResult()) {
        flag = false;
      }
      assertResults.add(assertResult);
    }
    TApiResult.setRspAsserts(assertResults);
    return flag;
  }
  
  //单个断言
  private AssertResult assertion(Assert assertion, Map<String, Object> caseVars, TApiResult tApiResult) {
    //处理接口变量类型的参数
    if (assertion.getExtractType().equals("apiVar")) {
      String expectValue = assertion.getExpectValue();
      if (expectValue == null) {
        assertion.setExtractType("null");
        assertion.setExpectValue("");
      } else {
        Object o = caseVars.get(expectValue);
        String objRealType = ApiUtil.getObjRealType(o);
        assertion.setExtractType(objRealType);
        assertion.setExpectValue(o.toString());
      }
    }
    AssertResult assertResult = new AssertResult();
    BeanUtils.copyBeanProp(assertResult, assertion);
    String extractExpress = assertion.getExtractExpress() == null ? "" : assertion.getExtractExpress();
    switch (assertion.getDataSource()) {
      case "bodyJson":
        //case "bodyJson"
        if (tApiResult.getRspBodyType().equals("json")) {
//                    JsonPath json = new JsonPath(tApiResult.getRspBody());
          Object value = null;
          try {
            value = JsonPath.read(tApiResult.getRspBody(), extractExpress);
//                        value = json.get(extractExpress);
          } catch (Exception e) {
            assertResult.setRealType("null");
            assertResult.setRealValue("");
            break;
          }
          String realType = ApiUtil.getObjRealType(value);
          if (realType.equals("null")) {
            assertResult.setRealType(realType);
            assertResult.setRealValue("");
          } else {
            assertResult.setRealType(realType);
            assertResult.setRealValue(value.toString());
          }
        } else {
          assertResult.setRealType("null");
          assertResult.setRealValue("");
        }
        break;
      case "bodyXml":
        if (tApiResult.getRspBodyType().equals("bodyXml")) {
          XmlPath xmlPath = new XmlPath(tApiResult.getRspBody());
          Object value = null;
          try {
            value = xmlPath.get(extractExpress);
          } catch (Exception e) {
            assertResult.setRealType("null");
            assertResult.setRealValue("");
            break;
          }
          String realType = ApiUtil.getObjRealType(value);
          if (realType.equals("null")) {
            assertResult.setRealType(realType);
            assertResult.setRealValue("");
          } else {
            assertResult.setRealType(realType);
            assertResult.setRealValue(value.toString());
          }
        } else {
          assertResult.setRealType("null");
          assertResult.setRealValue("");
        }
        break;
      case "bodyReg":
        String value = MyStringUtils.extractValue(tApiResult.getRspBody(), assertion.getExtractExpress(), 1);
        if (value == null) {
          assertResult.setRealType("null");
          assertResult.setRealValue("");
        } else {
          assertResult.setRealType("string");
          assertResult.setRealValue(value);
        }
        break;
      case "code":
        String realType = ApiUtil.getObjRealType(tApiResult.getRspStatusCode());
        if (realType.equals("null")) {
          assertResult.setRealType("null");
          assertResult.setRealValue("");
        } else {
          assertResult.setRealType(realType);
          assertResult.setRealValue(String.valueOf(tApiResult.getRspStatusCode()));
        }
        break;
      case "header":
        WebHeader header = null;
        if (tApiResult.getRspHeaders() != null) {
          header = tApiResult.getRspHeaders().stream().filter(x -> x.getKey().equals(extractExpress)).findFirst().orElse(null);
        }
        if (header == null) {
          assertResult.setRealType("null");
        } else {
          assertResult.setRealType("string");
        }
        assertResult.setRealValue(header.getValue());
        break;
      default:
    }
    boolean assertionResult = ApiUtil.getAssertionResult(assertion, assertResult.getRealType(), assertResult.getRealValue());
    assertResult.setResult(assertionResult);
    return assertResult;
  }
}
