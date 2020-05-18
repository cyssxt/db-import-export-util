package com.cyssxt.excel.core;

import com.cyssxt.excel.annotations.ExcelField;
import com.cyssxt.excel.annotations.ExcelView;
import com.cyssxt.excel.constant.ExcelMessage;
import com.cyssxt.excel.model.ExcelException;
import com.cyssxt.excel.model.ExportFieldInfo;
import com.cyssxt.excel.model.ViewInfo;
import com.cyssxt.excel.model.View;
import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class DefaultHelper<T> implements Helper<T> {
    public ViewInfo parse(Class<? extends View> view) throws ExcelException {
        Field[] fields = view.getDeclaredFields();
        ExcelView excelView = view.getDeclaredAnnotation(ExcelView.class);
        String tableName = excelView.name();
        ViewInfo exportView = new ViewInfo();
        exportView.setTableName(tableName);
        if(excelView==null){
            throw new ExcelException(ExcelMessage.EXCEL_ERROR);
        }
        List<ExportFieldInfo> fieldInfoList = new ArrayList<>();
        for(Field field:fields){
            ExportFieldInfo fieldInfo = new ExportFieldInfo();
            ExcelField excelField = field.getDeclaredAnnotation(ExcelField.class);
            if(excelField==null){
                continue;
            }
            String name = excelField.name();
            fieldInfo.setName(name);
            fieldInfo.setNotNull(excelField.notNull());
            fieldInfo.setIndex(excelField.index());
            fieldInfo.setTitle(excelField.title());
            fieldInfo.setSort(excelField.sort());
            fieldInfo.setGenerator(excelField.generator());
            fieldInfo.setDefaultValue(excelField.defaultValue());
            fieldInfoList.add(fieldInfo);
            String fieldName = field.getName();
            try {
                PropertyDescriptor pd = new PropertyDescriptor(fieldName,view);
                Method write = pd.getWriteMethod();
                Method read = pd.getReadMethod();
                fieldInfo.setWrite(write);
                fieldInfo.setExport(excelField.export());
                fieldInfo.setRead(read);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }
        fieldInfoList.sort(Comparator.comparingInt(ExportFieldInfo::getSort));
        exportView.setFields(fieldInfoList);
        return exportView;
    }
    public abstract List<Map<String,Object>> execute(DataSource dataSource,Class<? extends View> view,Decorter<T> decorter) throws ExcelException, IOException;

    @Override
    public List<Map<String,Object>> run(DataSource dataSource, Class<? extends View> view, Decorter<T> decorter) throws ExcelException, IOException {
        before();
        List<Map<String,Object>> dataList =execute(dataSource,view,decorter);
        after();
        return dataList;
    }

    public void before(){}
    public void after(){}
}
