package com.cyssxt.excel.core;

import com.cyssxt.excel.generator.DefaultValueFieldGenerator;
import com.cyssxt.excel.generator.FieldGenerator;
import com.cyssxt.excel.model.*;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ImportHelper extends DefaultHelper<ExcelDataInfo> {

    @Override
    public List<Map<String,Object>> execute(DataSource dataSource, Class<? extends View> view, Decorter<ExcelDataInfo> decorter) throws ExcelException, IOException {
        ViewInfo viewInfo = parse(view);
        ExcelDataInfo excelDataInfo = decorter.get();
        Map<String,Object> params = excelDataInfo.getParams();
        String tableName = viewInfo.getTableName();
        List<ExportFieldInfo> exportFieldInfos = viewInfo.getFields();
        List<String> fields = new ArrayList<>();

        List<String> param = new ArrayList<>();
        for (ExportFieldInfo exportFieldInfo : exportFieldInfos) {
            String name = exportFieldInfo.getName();
            if (name == null || "".equals(name)) {
                continue;
            }
            fields.add(name);
            param.add("?");
        }
        int length = exportFieldInfos.size();
//        for (int i = 0; i < length; i++) {
//            String name = exportFieldInfo.getName();
//            param.add("?");
//        }
//        String temp = String.format("(%s)",String.join(","));
//        List<String> params = new ArrayList<>();
        List<List<Object>> dataList = excelDataInfo.getDatas();
        List<String> headers = excelDataInfo.getHeaders();
        int total = dataList.size();
//        for(int i=0;i<total;i++){
//            params.add(temp);
//        }


        Connection connection = null;
        List<Map<String,Object>> result = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = String.format("insert into %s(%s) values (%s)", tableName, String.join(",", fields), String.join(",", param));
            System.out.println(sql);
            Map<String, Integer> headerMap = new HashMap<>();
            int headerSize = headers.size();
            for (int i = 0; i < headerSize; i++) {
                headerMap.put(headers.get(i), i);
            }
            for (int i = 0; i < total; i++) {
                List<Object> datas = dataList.get(i);
                if(datas.size()!=headerSize){
                    continue;
                }
                Map data = execute(connection, sql, length, datas, exportFieldInfos, headerMap, i,params);
                if(data!=null) {
                    result.add(data);
                }
            }
            System.out.println("success");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public Map<String,Object> execute(Connection connection, String sql, int length, List<Object> objects, List<ExportFieldInfo> exportFieldInfos, Map<String, Integer> headerMap, int indexS,Map<String,Object> parentParams) {
        List<String> temp = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int j = 0; j < length; j++) {
                ExportFieldInfo fieldInfo = exportFieldInfos.get(j);
                String name = fieldInfo.getName();
                String title = fieldInfo.getTitle();
                Integer index = headerMap.get(title);
                boolean isIndex = fieldInfo.isIndex();
                Object value = null;
                if (isIndex) {
                    value = indexS;
                } else {
                    if (index != null) {
                        value = objects.get(index);
                    }
                    Class<? extends FieldGenerator> generator = fieldInfo.getGenerator();
                    String defaultValue = fieldInfo.getDefaultValue();
                    if (generator != null && generator != DefaultValueFieldGenerator.class) {
                        try {
                            value = generator.newInstance().generator(fieldInfo, headerMap, objects,param,parentParams);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if(name==null||"".equals(name)){
                        continue;
                    }
                    if (value == null) {
                        value = defaultValue;
                    }
                    if (fieldInfo.isNotNull() && (value == null || "".equals(value))) {
                        return null;
                    }
                    temp.add(value + "");
                }
                param.put(name, value);
                ps.setObject(j + 1, value);
            }
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("e={}",e);
            e.printStackTrace();
            log.error(e + String.join(",", temp) + "");
            System.out.println(e + String.join(",", temp) + "");
        }finally {
        }
        return param;
    }

    public static void main(String[] args) throws ExcelException {
//        Helper helper = new ImportHelper();
//        HikariConfig config = new HikariConfig();
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/bus-data-import");
//        config.setUsername("root");
//        config.setPassword("!QAZ2wsx");
//        HikariDataSource hikariDataSource = new HikariDataSource(config);
//        helper.run(hikariDataSource, TestView.class, new Decorter() {
//            @Override
//            public ExcelDataInfo get() {
//                File file = new File("aaaaaaa.xlsx");
//                System.out.println(file.getAbsoluteFile());
//                ExcelDataInfo excelDataInfo = null;
//                try {
//                    excelDataInfo = ExcelUtil.readData2007(file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(excelDataInfo.getDatas());
//                return excelDataInfo;
//            }
//        });
    }
}
