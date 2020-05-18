package com.cyssxt.excel.generator;

import com.cyssxt.excel.model.ExportFieldInfo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class TimeGenerator implements FieldGenerator<Timestamp>{
    @Override
    public Timestamp generator(ExportFieldInfo fieldInfo, Map<String, Integer> headerMap, List<Object> dataList, Map<String, Object> params, Map<String, Object> parentParams) {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }
}
