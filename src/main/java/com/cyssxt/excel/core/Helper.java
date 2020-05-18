package com.cyssxt.excel.core;

import com.cyssxt.excel.model.ExcelException;
import com.cyssxt.excel.model.View;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Helper<T> {
    List<Map<String,Object>> run(DataSource dataSource, Class<? extends View> view, Decorter<T> decorter) throws ExcelException, IOException;
}
