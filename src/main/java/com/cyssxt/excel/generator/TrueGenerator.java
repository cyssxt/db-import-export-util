package com.cyssxt.excel.generator;

import com.cyssxt.excel.model.ExportFieldInfo;

import java.util.List;
import java.util.Map;

public class TrueGenerator implements FieldGenerator<Boolean> {

    @Override
    public Boolean generator(ExportFieldInfo fieldInfo, Map<String, Integer> headerMap, List<Object> dataList,Map<String,Object> param,Map<String,Object> parentParams) {
        return true;
    }
}
