package com.auto.test.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auto.test.common.exception.ServiceException;
import com.auto.test.config.ApplicationConfig;
import com.auto.test.config.apiggs.CostomPostmanRender;
import com.auto.test.config.excel.UploadInterfaceListener;
import com.auto.test.dao.TAutoInterfaceDao;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.entity.TAutoModel;
import com.auto.test.model.constant.FileType;
import com.auto.test.model.excel.TAutoInterfaceExport;
import com.auto.test.model.excel.TAutoInterfaceImport;
import com.auto.test.model.po.BodyData;
import com.auto.test.model.po.Query;
import com.auto.test.model.po.WebHeader;
import com.auto.test.model.postman.PostmanCollection;
import com.auto.test.model.postman.PostmanFolder;
import com.auto.test.model.postman.PostmanItem;
import com.auto.test.model.postman.PostmanReader;
import com.auto.test.model.vo.RepositoryVo;
import com.auto.test.service.TAutoInterfaceClassifyService;
import com.auto.test.service.TAutoInterfaceService;
import com.auto.test.service.TAutoModelService;
import com.auto.test.utils.repository.RepositoryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.apigcc.core.Apigcc;
import com.github.apigcc.core.Context;
import com.github.apigcc.core.common.postman.Folder;
import com.github.apigcc.core.common.postman.Postman;
import io.swagger.models.*;
import io.swagger.models.parameters.Parameter;
import io.swagger.parser.SwaggerParser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
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
  @Autowired
  ApplicationConfig config;
  @Resource
  TAutoInterfaceDao interfaceDao;
  @Autowired
  private TAutoInterfaceService interfaceService;
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
                  JSONObject jsonParameter = JSONObject.parseObject(JSON.toJSONString(parameter));
                  //           JSONObject.parseObject(parameter.toString());
                  if ("body".equals(in)) {
                    autoInterface.setReqBodyType("raw");
                    
                    autoInterface.setReqBodyJson(JSON.toJSONString(jsonParameter.get("schema")));
                  } else if ("formData".equals(in)) {
                    autoInterface.setReqBodyType("formdata");
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
  
  @Override
  public Boolean excelImport(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    String fileType = fileName.substring(fileName.lastIndexOf("."));
    if(StringUtils.isEmpty(fileType)|| !fileType.equals(FileType.XLSX.value)){
      throw new ServiceException( "文件类型异常，请上传 XLSX格式文件");
    }
    try {
      EasyExcel.read(file.getInputStream(), TAutoInterfaceImport.class, new UploadInterfaceListener(interfaceService)).sheet().doRead();
      
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  
    return true;
  }
  
  @SneakyThrows
  @Override
  public void download(HttpServletResponse response) {
    // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
    try {
      response.setContentType("application/vnd.ms-excel");
      response.setCharacterEncoding("utf-8");
      // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
      String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
      response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
      // 这里需要设置不关闭流
      List<TAutoInterface> list = interfaceDao.selectAll();
      List<TAutoInterfaceExport> interfaceExcels = TAutoInterfaceExport.toList(list);
      EasyExcel.write(response.getOutputStream(), TAutoInterfaceExport.class).autoCloseStream(Boolean.FALSE).sheet("模板")
          .doWrite(interfaceExcels);
    } catch (Exception e) {
      // 重置response
      response.reset();
      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      Map<String, String> map = new HashMap<String, String>();
      map.put("status", "failure");
      map.put("message", "下载文件失败" + e.getMessage());
      response.getWriter().println(JSON.toJSONString(map));
    }
    
  }
  
  @Override
  public void checkInterfaceImport(TAutoInterfaceImport interfaceImport) {
    QueryWrapper<TAutoInterface> wrapper = new QueryWrapper<>();
    wrapper.eq("name", interfaceImport.getName());
    List<TAutoInterface> autoInterfaceList =  list(wrapper);
    if (autoInterfaceList != null && autoInterfaceList.size() > 0) {
      throw new ServiceException(interfaceImport.getName() + "：名称重复");
    }
    QueryWrapper<TAutoInterfaceClassify> classifyQueryWrapper = new QueryWrapper<>();
    classifyQueryWrapper.eq("name", interfaceImport.getClassifyName());
    List<TAutoInterfaceClassify> classifyList = classifyService.list(classifyQueryWrapper);
    if (classifyList == null || classifyList.size() == 0) {
      throw new ServiceException(interfaceImport.getClassifyName() + "：分组名称不存在，请先创建分组");
    }
    
  }
  
  @Override
  public Integer springMVCCodeImport(String url, String moduleId) {
    RepositoryVo vo = new RepositoryVo().setCodeUrl(url).setModuleId(moduleId).setBasePath(config.getBasePath());
    RepositoryUtils.checkout(vo);
    String localPath = RepositoryUtils.getLocalPath(vo);
   
    Context context = new Context();
    context.setId(moduleId);
    context.setName(moduleId);
    context.addSource(Paths.get(localPath));
    Apigcc apigcc = new Apigcc(context);
    apigcc.parse();
    apigcc.render();
    CostomPostmanRender costomPostmanTreeHandler = new CostomPostmanRender();
  
    Postman postman =costomPostmanTreeHandler.build(apigcc.getProject());
   
    List<Folder>  postmanItems = postman.getItem();
    Integer count =0;
    if(postmanItems!=null){
     // postmanItems.stream().forEach(postmanItem -> {
        for(Folder postmanItem: postmanItems){
        TAutoInterfaceClassify interfaceClassify = new TAutoInterfaceClassify();
        interfaceClassify.setName(postmanItem.getName());
          interfaceClassify.setModuleId(moduleId);
        classifyService.save(interfaceClassify);
        List<Folder> items = postmanItem.getItem();
        if(items!=null&&items.size()>0){
          for(Folder postmanItemSubitem: items){
        //  items.stream().forEach(postmanItemSubitem -> {
            TAutoInterface autoInterface = new TAutoInterface(postmanItemSubitem);
            autoInterface.setClassifyId(interfaceClassify.getId());
          save(autoInterface);
            count++;
          }
        }
      }
    }
    return count;
  }
  
  @SneakyThrows
  @Override
  public Integer postManImport(MultipartFile file, String moduleId) {
    String fileName = file.getOriginalFilename();
    String fileType = fileName.substring(fileName.lastIndexOf("."));
    if(StringUtils.isEmpty(fileType)||!fileType.equals(FileType.JSON.value)){
      throw new ServiceException( "文件类型异常，请上传json格式文件");
    }
    PostmanReader reader = new PostmanReader();
    PostmanCollection postmanCollection = reader.readCollectionFile(file.getInputStream());
    Integer count =0;
   
    List<PostmanFolder>  postmanItems = postmanCollection.getItem();
    if(postmanItems!=null){
      
        for(PostmanFolder postmanItem: postmanItems){
        TAutoInterfaceClassify interfaceClassify = new TAutoInterfaceClassify();
        interfaceClassify.setName(postmanItem.getName());
        interfaceClassify.setModuleId(moduleId);
        classifyService.save(interfaceClassify);
        List<PostmanItem> items = postmanItem.getItem();
        if(items!=null&&items.size()>0){
            for(PostmanItem postmanItemSubitem: items){
            List<PostmanItem>  item = postmanItemSubitem.getItem();
            if(item!=null&&item.size()>0){
                for(PostmanItem itemSub: item){
                  TAutoInterface autoInterface = new TAutoInterface(itemSub);
                  autoInterface.setClassifyId(interfaceClassify.getId());
                  save(autoInterface);
                  count++;
                }
            }else{
              TAutoInterface autoInterface = new TAutoInterface(postmanItemSubitem);
              autoInterface.setClassifyId(interfaceClassify.getId());
              save(autoInterface);
              count++;
            }
          }
        }
      }
    }
    return count;
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