package com.cyssxt.excel.core;

import com.cyssxt.excel.TestView;
import com.cyssxt.excel.model.ExcelException;
import com.cyssxt.excel.model.ExportFieldInfo;
import com.cyssxt.excel.model.ViewInfo;
import com.cyssxt.excel.model.View;
import com.cyssxt.excel.util.ExcelUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ExportHelper extends DefaultHelper<Object> {


//    private static ExportHelper _instance;
//    public static ExportHelper getInstance(){
//        if(_instance==null){
//            _instance = new ExportHelper();
//        }
//        return _instance;
//    }

    public List<Map<String,Object>> execute(DataSource dataSource, Class<? extends View> view,Decorter<Object> decorter) throws ExcelException {
        return execute(dataSource,view,(String)null);
    }
    public List<Map<String,Object>> execute(DataSource dataSource, Class<? extends View> view,String sql) throws ExcelException {
        ViewInfo viewInfo = parse(view);
        Connection connection = null;
        if(sql==null || "".equals(sql)){
            sql = String.format("select * from %s", viewInfo.getTableName());
        }
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
            int count = resultSetMetaData.getColumnCount();
//            List<ExportFieldInfo> fieldInfos = exportViewInfo.getFields();
            Map<String,ExportFieldInfo> fieldInfoMap = viewInfo.getFieldMap();
            Map<String,String> columnNames = new HashMap<>();
            for(int i=1;i<=count;i++){
                String columnName = resultSetMetaData.getColumnName(i);
                columnNames.put(columnName.toLowerCase().replace("_",""),columnName);
            }
            List<Map> result = new ArrayList<>();
            Map<String,String> headers = new HashMap<>();
            while (resultSet.next()){
                Map<String,Object> item = new HashMap<>();
                Iterator<Map.Entry<String,String>> iterator = columnNames.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String,String> entry = iterator.next();
                    String alias = entry.getKey();
                    String columnName = entry.getValue();
                    ExportFieldInfo fieldInfo = fieldInfoMap.get(alias);
                    if(fieldInfo==null || !fieldInfo.isExport()){
                        continue;
                    }
                    headers.put(alias,fieldInfo.getTitle());
                    Object value = resultSet.getObject(columnName);
                    item.put(alias,value);
                }
                result.add(item);
            }
            HSSFWorkbook workbook=ExcelUtil.write(headers,result);
            workbook.write(new File(".test1.xlsx"));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws ExcelException {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/bus-data");
        config.setUsername("root");
        config.setPassword("!QAZ2wsx");
        HikariDataSource hikariDataSource = new HikariDataSource(config);
//        new ExportHelper().execute(hikariDataSource, TestView.class);
    }

}
