package com.cyssxt.excel.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class ExcelContext {

    private final static Map<String, DefaultHelper> helpers = new HashMap<>();
    private final static String EXPORT = "EXPORT";
    private final static String IMPORT = "IMPORT";
    static {
        helpers.put(EXPORT,new ExportHelper());
        helpers.put(IMPORT,new ImportHelper());
    }

    public static DefaultHelper get(String type){
        return helpers.get(type);
    }

}
