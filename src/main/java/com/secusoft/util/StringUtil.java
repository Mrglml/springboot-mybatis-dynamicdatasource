package com.secusoft.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

/**
 * 字符串工具
 * @author yaojiacheng
 * 2018年4月12日
 */
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(str==null || str.trim()=="" || str.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断多个字符串中是否含有空字符串
	 * @param strs
	 * @return
	 */
	public static boolean isAnyBlank(String... strs){
		for(String str_ : strs){
			if(isBlank(str_))
				return true;
		}
		return false;
	}
	
	/**
	 * 字符串脱敏
	 * @param str 需脱敏的字符串
	 * @param type 字符串类型：1.姓名 2.手机号 3.身份证号 4.银行卡号
	 * @return
	 */
	public static String getDesensitization(String str,int type){
		if(isBlank(str)){
			return null;
		}else{
			try{
				switch(type){
				case 1:
					StringBuffer name = new StringBuffer(str.substring(0, 1));
					for(int i=1;i<str.length();i++){
						name.append("*");
					}
					return name.toString();
				case 2:
					return str.substring(0,3)+"****"+str.substring(7,11);
				case 3:
					return "**************"+str.substring(str.length()-4, str.length());
				case 4:
					return str.substring(0,6)+"**********"+str.substring(str.length()-4, str.length());
				default:return null;
				}
			}catch (Exception e) {
				return null;
			}
		}
	}
	
	/**
	 * 获取业务流水凭证
	 * @return
	 */
	public static String generateTransactionId(){
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + UUIDString();
	}
	
	/**
	 * 获取UUID
	 * @return
	 */
	public static String UUIDString(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * urlencode转map
	 * @param str
	 * @return
	 */
	public static Map<String, String> analysisPars(String str){
		Map<String, String> result = Maps.newHashMap();
		String[] strs = str.split("&");
		for(String par : strs){
			result.put(par.split("=")[0], par.split("=")[1]);
		}
		return result;
	}
	
}
