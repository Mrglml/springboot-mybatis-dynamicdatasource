package com.secusoft.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 修改配置文件
 * @author yaojiacheng
 * 2018年4月12日
 */
public class PropertiesUtil {
	/**
	 * 
	 * @param path
	 * @param key
	 * @param value
	 * @return
	 */
    public static Boolean updatePro(String path, String key, String value) {
        Properties prop = new Properties();// 属性集合对象
        FileInputStream fis;
        try {
        	//读取配置文件内容，接着原内容继续添加
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
    
    /**
     * 
     * @param path
     * @param map
     * @return
     */
    public static Boolean updateProWithMap(String path, Map<String,String> map) {
        Properties prop = new Properties();// 属性集合对象
        //不进行读取，把之前的文件内容覆盖掉
//        FileInputStream fis;
//        try {
//            fis = new FileInputStream(path);
//            prop.load(fis);// 将属性文件流装载到Properties对象中
//            fis.close();// 关闭流
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
        for(Map.Entry<String,String> entry : map.entrySet()){
        	prop.setProperty(entry.getKey(), entry.getValue());
        }
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