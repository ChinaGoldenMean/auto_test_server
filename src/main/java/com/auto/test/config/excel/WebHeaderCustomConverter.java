package com.auto.test.config.excel;

import com.auto.test.common.handler.typehandler.ListTypeHandler;
import com.auto.test.model.po.ApiParam;
import com.auto.test.model.po.WebHeader;

public class WebHeaderCustomConverter extends HeaderCustomConverter {
  
  @Override
  public Class supportJavaTypeKey() {
    return   WebHeader.class;
  }
}
