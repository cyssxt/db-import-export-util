package com.cyssxt.excel.util;

import com.cyssxt.excel.model.ExcelDataInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtil {

    public static ExcelDataInfo readData(File file) throws IOException {
        ExcelDataInfo excelDataInfo = new ExcelDataInfo();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        int i=0;
        HSSFRow row = sheet.getRow(i);
        int lastRowNum = sheet.getLastRowNum();
        int lastColumn = row.getPhysicalNumberOfCells();
        System.out.println("lastColumn="+lastColumn);
        HSSFCell cell = null;
        List<List<Object>> result = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        int length = 0;
        for (int j=0;j<lastColumn;j++){
            cell = row.getCell(j);
            if(cell==null){
                length = j+1;
                break;
            }
            String value = cell.getStringCellValue();
            if(value==null || "".equals(value)){
                length = j+1;
                break;
            }
            headers.add(value);
        }
        i++;
        while (row!=null){
            row = sheet.getRow(i);
            List<Object> datas = new ArrayList<>();
            if(lastRowNum<i){
                break;
            }
            for(int j=0;j<lastColumn;j++){
                cell = row.getCell(j);
                if(cell==null){
                    break;
                }
                String value = cell.getStringCellValue();
                datas.add(value);
            }
            result.add(datas);
            i++;
        }
        excelDataInfo.setHeaders(headers);
        excelDataInfo.setDatas(result);
        return excelDataInfo;
    }
    public static ExcelDataInfo readData2007(File file,String sheetName) throws IOException {
        return readData2007(new FileInputStream(file),sheetName);
    }
    public static ExcelDataInfo readData2007(InputStream is,String sheetName) throws IOException {
        ExcelDataInfo excelDataInfo = new ExcelDataInfo();
        XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet sheet = hssfWorkbook.getSheet(sheetName);
        int i=0;
        XSSFRow row = sheet.getRow(i);
        int lastRowNum = sheet.getLastRowNum();
        int lastColumn = row.getPhysicalNumberOfCells();
        System.out.println("lastColumn="+lastColumn);
        XSSFCell cell = null;
        List<List<Object>> result = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        for (int j=0;j<lastColumn;j++){
            cell = row.getCell(j);
            if(cell==null){
                lastColumn = j+1;
                break;
            }
            String value = cell.getStringCellValue();
            if("Table 1".equals(value)){
                j=-1;
                i++;
                row = sheet.getRow(i);
                continue;
            }
            if(value==null || "".equals(value)){
                lastColumn = j+1;
                break;
            }
            headers.add(value);
        }
        i++;
        lastColumn = headers.size();
        while (row!=null){
            row = sheet.getRow(i);
            List<Object> datas = new ArrayList<>();
            if(lastRowNum<i){
                break;
            }
            for(int j=0;j<lastColumn;j++){
                cell = row.getCell(j);
                if(cell==null){
                    break;
                }
                CellType cellType= cell.getCellType();
                Object value = null;
                if(cellType== CellType.BLANK){
                    value = "";
                }else if(cellType== CellType.STRING){
                    value = cell.getStringCellValue();
                }
                else if(cellType== CellType.NUMERIC){
                    String dataFormat = cell.getCellStyle().getDataFormatString();
                    if(dataFormat!=null && dataFormat.contains("yy")){
                        value = cell.getDateCellValue();
                    }else {
                        DecimalFormat df = new DecimalFormat("#");
                        value = df.format(cell.getNumericCellValue());
                    }
                }else if (cellType == CellType.FORMULA){
                    cell.setCellType(CellType.STRING);
                    value = cell.getStringCellValue();
                }
                datas.add(value);
            }
            result.add(datas);
            i++;
        }
        excelDataInfo.setHeaders(headers);
        excelDataInfo.setDatas(result);
        return excelDataInfo;
    }

    public static HSSFWorkbook write(Map<String,String> headers, List<Map> result) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        int row=0;
        HSSFRow hssfRow = sheet.createRow(row);
        Iterator<Map.Entry<String,String>> iterator = headers.entrySet().iterator();
//        int i=0;
        for (int i=0;iterator.hasNext();i++){
            Map.Entry<String,String> entry = iterator.next();
            HSSFCell cell = hssfRow.createCell(i);
            cell.setCellValue(entry.getValue());
        }
        row++;
        for(Map<String,Object> item:result){
            hssfRow = sheet.createRow(row);
            iterator = headers.entrySet().iterator();
            int j=0;
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                HSSFCell cell = hssfRow.createCell(j);
                Object value = item.get(key);
                if(value!=null) {
                    cell.setCellValue(value + "");
                }
                j++;
            }
            row++;
        }
        return workbook;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("bbb.xlsx");
        System.out.println(file.getAbsoluteFile());
        ExcelDataInfo excelDataInfo = readData2007(file,"学期信息");
        System.out.println(excelDataInfo);
    }
}
