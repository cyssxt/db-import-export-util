package com.cyssxt.excel.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ViewInfo {
    String tableName;
    List<ExportFieldInfo> fields;
    String title;

    public Map<String,ExportFieldInfo> getFieldMap(){
        return fields.stream().collect(Collectors.toMap(t->t.getName().toLowerCase().replace("_",""),t->t));
    }
}
