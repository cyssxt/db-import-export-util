package com.cyssxt.excel.annotations;

import com.cyssxt.carservice.parse.DataParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelView {
    String title() default "";
    int sort() default 0;
    String name() default "";
    String exportName() default "";
    String excelName() default "";
    String importName() default "";
    boolean unique() default  false;
    Class dataParse() default DataParser.class;
}
