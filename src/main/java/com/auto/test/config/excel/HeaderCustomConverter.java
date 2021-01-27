package com.auto.test.config.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auto.test.model.po.WebHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * String and string converter
 *
 * @author Jiaju Zhuang
 */
public abstract class HeaderCustomConverter implements Converter<List<WebHeader>> {

    
//    @Override
//    public List supportJavaTypeKey() {
//        return  JSONObject.parseArray(getTypeClass());
//    }
    
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 这里读的时候会调用
     *
     * @param cellData
     *            NotNull
     * @param contentProperty
     *            Nullable
     * @param globalConfiguration
     *            NotNull
     * @return
     */
    @Override
    public List<WebHeader> convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) {

        return JSONObject.parseArray(cellData.getStringValue(), WebHeader.class);
    }
    
    
    
    /**
     * 这里是写的时候会调用 不用管
     *
     *            NotNull
     * @param contentProperty
     *            Nullable
     * @param globalConfiguration
     *            NotNull
     * @return
     */
    @Override
    public CellData convertToExcelData(List<WebHeader> list, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        List<Object> newList = new ArrayList();
    if(list!=null&&list.size()>0){
        list.stream().forEach(o -> {
            String str =JSON.toJSONString(o);
            Object obj =   JSONObject.toJSON(o);
            newList.add(str);
        });
        return new CellData(newList);
    }

        
        return  new CellData(list);
      //  return new CellData(CellDataTypeEnum.EMPTY,JSONArray.toJSONString(list));
    }

}
