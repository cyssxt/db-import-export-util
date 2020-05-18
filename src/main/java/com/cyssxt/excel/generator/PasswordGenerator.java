package com.cyssxt.excel.generator;

import com.cyssxt.excel.model.ExportFieldInfo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class PasswordGenerator implements FieldGenerator<String> {

    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    @Override
    public String generator(ExportFieldInfo fieldInfo, Map<String, Integer> headerMap, List<Object> dataList, Map<String, Object> params, Map<String, Object> parentParams) {
        return stringToMD5("123456");
    }
}
