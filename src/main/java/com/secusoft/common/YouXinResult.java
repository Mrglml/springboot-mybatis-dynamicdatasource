package com.secusoft.common;

import java.io.Serializable;
import java.util.Date;

import com.secusoft.util.DateUtil;

/**
 * 接口返回统一格式
 * @author yjc
 * 2017年4月14日
 */
public class YouXinResult<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	//为兼容IOS，将boolean换成String
	private String success = "true";
	
	//为兼容IOS，将Integer换成String
	private String code;
	
	private String errorMsg;
	
	private T data;
	
	private String nextUpdateTime;
	
	public static <T> YouXinResult<T> success(T data){
		YouXinResult<T> result = new YouXinResult<T>();
		result.setData(data);
		return result;
	}
	
	public static <T> YouXinResult<T> success(String code,T data){
		YouXinResult<T> result = new YouXinResult<T>();
		result.data=data;
		result.code=code;
		return result;
	}
	
	public static <T> YouXinResult<T> fail(String errorMsg){
		YouXinResult<T> result = new YouXinResult<T>();
		result.setErrorMsg(errorMsg);
		return result;
	}
	
	public static <T> YouXinResult<T> fail(String errorMsg,String code){
		YouXinResult<T> result = new YouXinResult<T>();
		result.setErrorMsg(errorMsg, code);
		return result;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success?"true":"false";
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.success="false";
		this.errorMsg = errorMsg;
		this.code = "500";
	}
	
	public void setErrorMsg(String errorMsg,String code) {
		this.success="false";
		this.errorMsg = errorMsg;
		this.code=code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
		this.code = "200";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNextUpdateTime() {
		return nextUpdateTime;
	}

	public void setNextUpdateTime(Date nextUpdateTime) {
		if(nextUpdateTime!=null){
			this.nextUpdateTime = DateUtil.parseDateStr(nextUpdateTime, DateUtil.format_YYYYMMDD);
		}
	}
	
}
