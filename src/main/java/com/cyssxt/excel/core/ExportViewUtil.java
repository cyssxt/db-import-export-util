package com.cyssxt.excel.core;

import com.cyssxt.common.config.JSONEnum;
import com.cyssxt.common.reflect.ReflectUtils;
import com.cyssxt.excel.annotations.ExcelField;
import com.cyssxt.excel.annotations.ExcelView;
import com.cyssxt.excel.constant.ExcelMessage;
import com.cyssxt.excel.model.ExcelException;
import com.cyssxt.excel.model.ExportFieldInfo;
import com.cyssxt.excel.model.View;
import com.cyssxt.excel.model.ViewInfo;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ExportViewUtil {
    public static ViewInfo parse(Class<? extends View> view) throws ExcelException {
        Field[] fields = view.getDeclaredFields();
        ExcelView excelView = view.getDeclaredAnnotation(ExcelView.class);
        String tableName = excelView.name();
        ViewInfo exportView = new ViewInfo();
        exportView.setTitle(excelView.title());
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

    public static  <T extends View> XSSFWorkbook export(List<T> items,Class<T> view){
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            ViewInfo viewInfo = ExportViewUtil.parse(view);
            List<ExportFieldInfo> exportFieldInfos = viewInfo.getFields();
            XSSFSheet xssfSheet = workbook.createSheet(viewInfo.getTitle());
            int rowNum=0;
            XSSFRow row = xssfSheet.createRow(rowNum);
            int colLength = exportFieldInfos.size();
            for(int i=0;i<colLength;i++){
                ExportFieldInfo fieldInfo = exportFieldInfos.get(i);
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(fieldInfo.getTitle());
            }
            rowNum++;
            for(T orderInfoDto:items){
                row = xssfSheet.createRow(rowNum);
                for(int i=0;i<colLength;i++){
                    XSSFCell cell = row.createCell(i);
                    ExportFieldInfo fieldInfo = exportFieldInfos.get(i);
                    String name = fieldInfo.getName();
                    String value = null;
                    Method method = null;
                    Object instance = orderInfoDto;
                    String[] names = name.split("\\.");
                    for(int j=0;j<names.length;j++){
                        name = names[j];
                        if(instance!=null) {
                            if(instance instanceof JSONEnum){
                                instance = ((JSONEnum) instance).getMsg();
                            }else {
                                Map<String, Method> tempMap = ReflectUtils.getReadMapper(instance.getClass());
                                method = tempMap.get(name);
                                if (method != null) {
                                    try {
                                        instance = method.invoke(instance);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    instance = null;
                                }
                            }
                        }
                    }
                    value = instance!=null?instance.toString():null;
                    cell.setCellValue(value);
                }
                rowNum++;
            }

        } catch (ExcelException | IntrospectionException e) {
            e.printStackTrace();
        }
        return workbook;
    }

}
