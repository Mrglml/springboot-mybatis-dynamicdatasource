package com.secusoft;

import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.secusoft.dao.MonitorDao;
import com.secusoft.model.Monitor;

/**
 * 启动总入口
 * @author yjc
 * 2017年4月14日
 */
//@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class WebApplication extends SpringBootServletInitializer {
	
	//数据源
	public static List<Monitor> monitors;
    
    @Bean
	RestTemplate restTemplate() {
    	HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(60000);
        httpRequestFactory.setConnectTimeout(60000);
        httpRequestFactory.setReadTimeout(60000);
		return new RestTemplate(httpRequestFactory);
	}
    
    @Bean   
    public MultipartConfigElement multipartConfigElement() {   
            MultipartConfigFactory factory = new MultipartConfigFactory();  
            //// 设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;  
            factory.setMaxFileSize("10240KB"); //KB,MB  
            /// 设置总上传数据总大小  
            factory.setMaxRequestSize("102400KB");   
            //Sets the directory location where files will be stored.  
            //factory.setLocation("路径地址");  
            return factory.createMultipartConfig();   
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }

    public static void main(String[] args) throws Exception {
    	//从主数据库中查询所有数据源
        monitors = new MonitorDao().selectMonitors();
        /*//将数据源写入到配置文件中
        String separator = System.getProperty("file.separator");
		String configPath = System.getProperty("user.dir")+separator+"config"+separator+"application-dev.properties";
		Map<String,String> map = new HashMap<String,String>();
		StringBuffer datasources = new StringBuffer("[");
		for(int i=0;i<monitors.size();i++){
			Monitor monitor = monitors.get(i);
			datasources.append("\""+monitor.getDbcode()+"\"");
			if(i<monitors.size()-1){
				datasources.append(",");
			}
			String dataSourcName = "spring.datasource."+monitor.getDbcode()+".";
			map.put(dataSourcName+"driver-class-name", "com.mysql.jdbc.Driver");
			map.put(dataSourcName+"url", "jdbc:mysql://"+ monitor.getDbip()+":"+ monitor.getDbport()+"/"+ monitor.getDbname()+"?characterEncoding=utf-8");
			map.put(dataSourcName+"username", monitor.getDbuser());
			map.put(dataSourcName+"password", monitor.getDbpswd());
		}
		datasources.append("]");
		map.put("datasources", datasources.toString());
        PropertiesUtil.updateProWithMap(configPath, map);*/
        SpringApplication.run(WebApplication.class, args);
    }
    
}