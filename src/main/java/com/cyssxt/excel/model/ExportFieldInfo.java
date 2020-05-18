package com.cyssxt.excel.model;

import com.cyssxt.excel.generator.FieldGenerator;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ExportFieldInfo {
    String name;
    Method write;
    Method read;
    String title;
    Integer sort;
    Class type;
    boolean export;
    boolean notNull;
    boolean index;
    Class<FieldGenerator> generator;
    String defaultValue;
}
