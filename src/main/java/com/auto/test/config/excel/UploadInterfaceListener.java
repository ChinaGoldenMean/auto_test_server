package com.auto.test.config.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.auto.test.common.exception.ServiceException;
import com.auto.test.dao.TAutoInterfaceDao;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.model.excel.TAutoInterfaceExport;
import com.auto.test.model.excel.TAutoInterfaceImport;
import com.auto.test.service.TAutoInterfaceClassifyService;
import com.auto.test.service.TAutoInterfaceService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class UploadInterfaceListener extends AnalysisEventListener<TAutoInterfaceImport> {
  @Resource
  private TAutoInterfaceClassifyService classifyService;
  /**
   * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
   */
  private static final int BATCH_COUNT = 5;
  List<TAutoInterfaceImport> list = new ArrayList();
  /**
   * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
   */
  private TAutoInterfaceService uploadDAO;
  
  public UploadInterfaceListener() {
    // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
    //    uploadDAO = new TAutoInterfaceService();
  }
  
  /**
   * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
   *
   * @param uploadDAO
   */
  public UploadInterfaceListener(TAutoInterfaceService uploadDAO) {
    this.uploadDAO = uploadDAO;
  }
  
  /**
   * 这个每一条数据解析都会来调用
   *
   * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
   * @param context
   */
  @Override
  public void invoke(TAutoInterfaceImport data, AnalysisContext context) {
    log.info("解析到一条数据:{}", JSON.toJSONString(data));
    list.add(data);
    // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
    if (list.size() >= BATCH_COUNT) {
      saveData();
      // 存储完成清理 list
      list.clear();
    }
  }
  
  /**
   * 所有数据解析完成了 都会来调用
   *
   * @param context
   */
  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {
    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    saveData();
    log.info("所有数据解析完成！");
  }
  
  private Boolean checkObjIsNull(TAutoInterfaceImport interfaceImport) {
    for (Field f : interfaceImport.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        if (f.get(interfaceImport) == null || "".equals(f.get(interfaceImport).toString())) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
          throw new ServiceException("请求参数不能为空");
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      
    }
    return true;
  }
  
  /**
   * 加上存储数据库
   */
  public void saveData() {
    log.info("{}条数据，开始存储数据库！", list.size());
    //校验
    
    if (list != null && list.size() > 0) {
      list.stream().forEach(interfaceImport -> {
        checkObjIsNull(interfaceImport);
        uploadDAO.checkInterfaceImport(interfaceImport);
      });
    }
    uploadDAO.saveBatch(TAutoInterfaceImport.toList(list));
    log.info("存储数据库成功！");
  }
  
}
