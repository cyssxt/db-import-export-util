package com.cyssxt.excel.generator;

import com.cyssxt.excel.model.ExportFieldInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UUIDGenerator extends DefaultValueFieldGenerator{

    @Override
    public String generator(ExportFieldInfo fieldInfo, Map<String, Integer> headerMap, List<Object> dataList, Map<String, Object> params, Map<String, Object> parentParams) {
        return UUID.randomUUID().toString().replace("-","");
    }
}
