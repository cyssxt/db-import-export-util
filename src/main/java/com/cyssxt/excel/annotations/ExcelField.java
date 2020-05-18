package com.cyssxt.excel.annotations;

import com.cyssxt.excel.generator.DefaultValueFieldGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
    String title() default "";
    int sort() default 0;
    String name() default "";
    String exportName() default "";
    String excelName() default "";
    String importName() default "";
    boolean unique() default  false;
    boolean export() default false;
    String defaultValue() default "";
    boolean notNull() default  false;
    boolean index() default  false;
    Class generator() default DefaultValueFieldGenerator.class;
}
