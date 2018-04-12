package com.secusoft.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.secusoft.exception.YouXinException;

/**
 * 统一异常处理
 * @author yjc
 * 2017年4月14日
 */
@ControllerAdvice
public class UnifiedExceptionHandler {
	
	private static final Logger logger = Logger.getLogger(UnifiedExceptionHandler.class);
	
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public YouXinResult<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    	YouXinResult<String> r = new YouXinResult<String>();
    	if(e instanceof YouXinException){
    		r.setErrorMsg(e.getMessage(),((YouXinException) e).getCode());
    	}else{
    		r.setErrorMsg("系统异常，请联系管理员或稍后再试");
    	}
    	logger.error("统一异常处理捕获了异常，请求地址："+req.getRequestURL(),e);
        return r;
    }
}
