package com.charlotte.core.util;


import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Charlotte
 */
public class FileUtils {

    public static String readText(String path) throws IOException {
        return readText(path, StandardCharsets.UTF_8.name());
    }

    public static String readText(String path, String charset) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[1024];
        int i;
        while ((i = inputStream.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, i, charset));
        }
        return sb.toString();
    }

    public static void writeText(String path, String text, String charset) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(path);
        outputStream.write(StringUtils.getBytes(text, charset));
    }

//    public static void main(String[] args) throws IOException {
//        System.out.println(readText("D:\\WorkProject\\盘锦\\视图数据\\v_ylz_yaopinmulu.sql", "GBK2312"));
//    }

}
