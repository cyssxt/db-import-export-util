package com.cyssxt.excel.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExcelDataInfo {
    List<String> headers;
    List<List<Object>> datas;
    Map<String,Object> params;
}
