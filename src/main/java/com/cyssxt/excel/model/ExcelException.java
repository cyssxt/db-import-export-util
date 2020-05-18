package com.cyssxt.excel.model;

public class ExcelException extends Exception {
    String error;
    public ExcelException(String excelError) {
        this.error = excelError;
    }
}
