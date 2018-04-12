package com.secusoft.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 修改配置文件
 * @author yaojiacheng
 * 2018年4月12日
 */
public class PropertiesUtil {
    //参数为要修改的文件路径  以及要修改的属性名和属性值
    public static Boolean updatePro(String path, String key, String value) {
        Properties prop = new Properties();// 属性集合对象
        FileInputStream fis;
        try {
            fis = new FileInputStream(path);
            prop.load(fis);// 将属性文件流装载到Properties对象中
            fis.close();// 关闭流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        prop.setProperty(key, value);
        // 文件输出流
        try {
            FileOutputStream fos = new FileOutputStream(path);
            // 将Properties集合保存到流中
            prop.store(fos, "");
            fos.close();// 关闭流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}