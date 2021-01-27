package com.auto.test.service;

import com.auto.test.entity.TAutoInterfaceBug;
import com.auto.test.model.dto.BugDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TAutoInterfaceBugService extends IService<TAutoInterfaceBug> {
  BugDto getBugById(String id);
}
