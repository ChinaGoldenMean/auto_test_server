package com.auto.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auto.test.dao.TAutoInterfaceClassifyDao;
import com.auto.test.dao.TAutoInterfaceDao;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.entity.TAutoModel;
import com.auto.test.model.po.BodyData;
import com.auto.test.model.po.Query;
import com.auto.test.model.po.WebHeader;
import com.auto.test.service.TAutoInterfaceClassifyService;
import com.auto.test.service.TAutoInterfaceService;
import com.auto.test.service.TAutoModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.*;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.parser.SwaggerParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (TAutoInterfaceClassify)表服务实现类
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
@Service
public class TAutoInterfaceServiceImpl extends ServiceImpl<TAutoInterfaceDao, TAutoInterface> implements TAutoInterfaceService {
  
  @Resource
  private TAutoInterfaceClassifyService classifyService;
  
  @Resource
  private TAutoModelService modelService;
  
  @Transactional
  @Override
  public Boolean swaggerImport(String apiUrl, String moduleId) {
    Swagger swagger = new SwaggerParser().read(apiUrl);
    if (swagger != null) {
      Map<String, Path> paths = swagger.getPaths();
      if (!paths.isEmpty()) {
        List<TAutoInterfaceClassify> interfaceClassifyList = TAutoInterfaceClassify.toInterfaceClassifyList(swagger.getTags(), moduleId);
        if (interfaceClassifyList != null) {
          classifyService.saveBatch(interfaceClassifyList);
          
        }
        saveDefinitions(swagger, moduleId);
        for (Map.Entry<String, Path> entry : paths.entrySet()) {
          String mapKey = entry.getKey();
          Path mapValue = entry.getValue();
          TAutoInterface autoInterface = new TAutoInterface();
          
          autoInterface.setDomain(swagger.getHost());
          autoInterface.setPath(swagger.getBasePath() + mapKey);
          
          Map<HttpMethod, Operation> operationMap = mapValue.getOperationMap();
          if (operationMap != null && !operationMap.isEmpty()) {
            List<WebHeader> webHeaderList = new ArrayList<>();
            List<BodyData> bodyDataList = new ArrayList<>();
            List<Query> queryList = new ArrayList<>();
            for (Map.Entry<HttpMethod, Operation> operationEntry : operationMap.entrySet()) {
              HttpMethod httpMethod = operationEntry.getKey();
              Operation operation = operationEntry.getValue();
              
              List<String> tags = operation.getTags();
              
              autoInterface.setMethod(httpMethod.name());
              
              autoInterface.setName(getName(mapKey));
              
              webHeaderList.addAll(getWebHeader(operation.getConsumes()));
              //  autoInterface.setReqHeader();
              autoInterface.setType(1);
              //  setParameter(autoInterface,operation);
              List<Parameter> parameters = operation.getParameters();
              if (parameters != null && parameters.size() > 0) {
                
                for (Parameter parameter : parameters) {
                  String in = parameter.getIn();
                  ;
                  JSONObject jsonParameter = JSONObject.parseObject(JSON.toJSONString(parameter));
                  //           JSONObject.parseObject(parameter.toString());
                  if ("body".equals(in)) {
                    autoInterface.setReqBodyType("raw");
                    
                    autoInterface.setReqBodyJson(JSON.toJSONString(jsonParameter.get("schema")));
                  } else if ("formData".equals(in)) {
                    autoInterface.setReqBodyType("form");
                    jsonParameter.remove("in");
                    bodyDataList.addAll(BodyData.json2BodyDataList(jsonParameter));
                    
                  } else if ("query".equals(in)) {
                    jsonParameter.remove("in");
                    queryList.addAll(Query.json2QueryList(jsonParameter));
                  }
                }
                
              }
              if (tags != null && tags.size() > 0) {
                //如果存在,则添加,否则创建新的集合
                String classifyName = tags.get(0);
                if (interfaceClassifyList != null) {
                  for (TAutoInterfaceClassify interfaceClassify : interfaceClassifyList) {
                    if (interfaceClassify.getName().equals(classifyName)) {
                      autoInterface.setClassifyId(interfaceClassify.getId());
                      
                    }
                  }
                  
                } else {
                  TAutoInterfaceClassify tAutoInterfaceClassify = new TAutoInterfaceClassify(mapKey);
                  
                  classifyService.save(tAutoInterfaceClassify);
                  autoInterface.setClassifyId(tAutoInterfaceClassify.getId());
                  
                }
                
              }
              
            }
            autoInterface.setReqBodyData(bodyDataList);
            autoInterface.setReqQuery(queryList);
            autoInterface.setReqHeader(webHeaderList);
            save(autoInterface);
          }
          
        }
      }
      
    }
    
    return true;
  }
  
  private List<WebHeader> getWebHeader(List<String> consumes) {
    List<WebHeader> webHeaders = new ArrayList<>();
    if (consumes != null && consumes.size() > 0) {
      for (String consume : consumes) {
        WebHeader webHeader = new WebHeader(consume);
        webHeaders.add(webHeader);
      }
    }
    return webHeaders;
  }
  
  private String getName(String name) {
    if (!StringUtils.isEmpty(name)) {
      name = name.substring(1).replace("/", "-");
    }
    
    return name;
  }
  
  private void saveDefinitions(Swagger swagger, String moduleId) {
    Map<String, Model> definitions = swagger.getDefinitions();
    if (definitions != null && !definitions.isEmpty()) {
      for (Map.Entry<String, Model> entry : definitions.entrySet()) {
        Model model = entry.getValue();
        TAutoModel tAutoModel = new TAutoModel(model, moduleId);
        modelService.save(tAutoModel);
      }
    }
  }
  
  private void setParameter(TAutoInterface autoInterface, Operation operation) {
    List<Parameter> parameters = operation.getParameters();
    if (parameters != null && parameters.size() > 0) {
      List<BodyData> bodyDataList = new ArrayList<>();
      List<Query> queryList = new ArrayList<>();
      for (Parameter parameter : parameters) {
        String in = parameter.getIn();
        JSONObject jsonObject = JSONObject.parseObject(parameter.toString());
        if ("body".equals(in)) {
          autoInterface.setReqBodyType("raw");
          autoInterface.setReqBodyJson(jsonObject.get("schema").toString());
        } else if ("formData".equals(in)) {
          autoInterface.setReqBodyType("form");
          jsonObject.remove("in");
          bodyDataList.addAll(BodyData.json2BodyDataList(jsonObject));
        } else if ("query".equals(in)) {
          jsonObject.remove("in");
          queryList.addAll(Query.json2QueryList(jsonObject));
        }
      }
      autoInterface.setReqBodyData(bodyDataList);
      autoInterface.setReqQuery(queryList);
    }
  }
}