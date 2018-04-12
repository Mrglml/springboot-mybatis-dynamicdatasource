package com.secusoft.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring容器中的bean实例
 * @author yjc
 * 2017年6月22日
 */
@Component
public class MyWebContextFactory implements ApplicationContextAware {
	
	/** 
     * Spring 应用上下文环境 
     */  
    private static ApplicationContext applicationContext;

	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	/**
	 * 根据bean名称获取bean实例
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	public static Object getBean(String beanName){  
		try{
			if (!MyWebContextFactory.containsBean(beanName))  
				return null;
			return applicationContext.getBean(beanName);  
		}catch(Exception e){
			return null;
		}
    }
	
	public static boolean containsBean(String name) {  
        return applicationContext.containsBean(name);  
    }

}
