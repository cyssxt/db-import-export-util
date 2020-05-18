package com.cyssxt.excel.generator;

import com.cyssxt.excel.model.ExportFieldInfo;

import java.util.List;
import java.util.Map;

public class DefaultValueFieldGenerator implements FieldGenerator<String> {


    @Override
    public String generator(ExportFieldInfo fieldInfo, Map<String, Integer> headerMap, List<Object> dataList, Map<String, Object> params, Map<String, Object> parentParams) {
        return "";
    }
}
