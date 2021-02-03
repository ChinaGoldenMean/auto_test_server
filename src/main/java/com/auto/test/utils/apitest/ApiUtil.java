package com.auto.test.utils.apitest;

import com.auto.test.model.po.Assert;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.SSLConfig.sslConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

@Slf4j
public class ApiUtil {
  public static String getObjRealType(Object object) {
    if (object == null) {
      return "null";
    } else {
      if (object instanceof Boolean) {
        return "boolean";
      } else if (object instanceof Byte || object instanceof Short || object instanceof Integer
          || object instanceof Long || object instanceof Float || object instanceof Double) {
        return "number";
      } else {
        return "string";
      }
    }
  }
  
  public static RequestSpecification given() {
    RequestSpecification requestSpecification = RestAssured.given();
    RestAssured.config = RestAssured.config().sslConfig(sslConfig().relaxedHTTPSValidation());
    RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
    Map<String, Object> httpClientParams = new HashMap<String, Object>();
    httpClientParams.put("http.connection.timeout", 60000);
    httpClientParams.put("http.socket.timeout", 60000);
    httpClientParams.put("http.connection.manager.timeout", 60000);
    RestAssured.config = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().addParams(httpClientParams));
    RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
    requestSpecification.config(RestAssured.config);
    return requestSpecification;
  }
  
  public static Object getRealObj(String object, Integer type) {
    if (type.equals(1)) {
      return object;
    } else if (type.equals(2)) {
      Double aDouble = new Double(0);
      try {
        aDouble = Double.valueOf(object);
      } catch (Exception e) {
      }
      return aDouble;
    } else if (type.equals(3)) {
      if (object.toLowerCase().equals("true")) {
        return true;
      } else {
        return false;
      }
    } else if (type.equals(4)) {
      return null;
    } else {
      return object;
    }
  }
  
  public static boolean getAssertionResult(Assert assertion, String realType, String realValue) {
    if (!assertion.getExtractType().equals(realType)) {
      return false;
    }
    switch (assertion.getExpectRelation()) {
      // case "等于":
      case 0:
        String expectValue = assertion.getExpectValue();
        if (!StringUtils.isEmpty(expectValue)&&expectValue.equals(realValue)) {
          return true;
        }
        break;
      // case "大于":
      case 1:
        if (realType.equals("number")) {
          Double aDouble = Double.valueOf(assertion.getExpectValue());
          Double aDouble1 = Double.valueOf(realValue);
          if (aDouble < aDouble1) {
            return true;
          }
        }
        break;
      // case "大于等于":
      case 2:
        if (realType.equals("number")) {
          Double aDouble = Double.valueOf(assertion.getExpectValue());
          Double aDouble1 = Double.valueOf(realValue);
          if (aDouble <= aDouble1) {
            return true;
          }
        }
        break;
      //  case "小于":
      case 3:
        if (realType.equals("number")) {
          Double aDouble = Double.valueOf(assertion.getExpectValue());
          Double aDouble1 = Double.valueOf(realValue);
          if (aDouble > aDouble1) {
            return true;
          }
        }
        break;
      case 4:
        //case "小于等于":
        if (realType.equals("number")) {
          Double aDouble = Double.valueOf(assertion.getExpectValue());
          Double aDouble1 = Double.valueOf(realValue);
          if (aDouble >= aDouble1) {
            return true;
          }
        }
        break;
      //  case "包含":
      case 5:
        if (realValue.contains(assertion.getExpectValue())) {
          return true;
        }
        break;
      //  case "不包含":
      case 6:
        if (!realValue.contains(assertion.getExpectValue())) {
          return true;
        }
        break;
      //  case "长度等于":
      case 7:
        if (assertion.getExpectValue().length() == realValue.length()) {
          return true;
        }
        break;
      //  case "长度大于":
      case 8:
        if (realValue.length() > assertion.getExpectValue().length()) {
          return true;
        }
        break;
      //    case "长度大于等于":
      case 9:
        if (realValue.length() >= assertion.getExpectValue().length()) {
          return true;
        }
        break;
      // case "长度小于":
      case 10:
        if (realValue.length() < assertion.getExpectValue().length()) {
          return true;
        }
        break;
      //    case "长度小等于":
      case 11:
        if (realValue.length() <= assertion.getExpectValue().length()) {
          return true;
        }
        break;
      //  case "开始于":
      case 12:
        if (realValue.startsWith(assertion.getExpectValue())) {
          return true;
        }
        break;
      //  case "结束于":
      case 13:
        if (realValue.endsWith(assertion.getExpectValue())) {
          return true;
        }
        break;
      default:
    }
    return false;
  }
  
}
