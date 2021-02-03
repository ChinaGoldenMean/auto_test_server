package com.auto.test.service;

import com.alibaba.fastjson.JSONObject;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.model.dto.InterfaceClassifyParam;
import com.auto.test.model.excel.TAutoInterfaceImport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * (TAutoInterfaceClassify)表服务接口
 *
 * @author litiewang
 * @since 2020-12-21 15:50:39
 */
public interface TAutoInterfaceService extends IService<TAutoInterface> {
  Boolean swaggerImport(String apiUrl, String moduleId);
  Boolean excelImport(MultipartFile file);
  void download(HttpServletResponse response);
  void checkInterfaceImport(TAutoInterfaceImport interfaceImport);
  Integer springMVCCodeImport(String url, String moduleId);
  Integer postManImport(MultipartFile file, String moduleId);
}