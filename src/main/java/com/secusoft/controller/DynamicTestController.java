package com.secusoft.controller;

import io.swagger.annotations.Api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;
import com.secusoft.common.MyWebContextFactory;
import com.secusoft.common.YouXinResult;
import com.secusoft.common.database.DynamicDataSourceContextHolder;
import com.secusoft.common.database.DynamicRoutingDataSource;
import com.secusoft.model.Monitor;

/**
 * 动态增加、修改、删除数据源
 * @author yaojiacheng
 * 2018年4月17日
 */
@RestController
@RequestMapping("/dynamic")
@Api(value = "DynamicTestController", description = "数据源动态操作测试")
public class DynamicTestController {
	
	/**
	 * 在服务运行期间，增加第四个数据源
	 * 动态修改和删除数据源的方法和此方法类似，故不再演示
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/addDataSource")
	public YouXinResult<String> addDataSource(HttpServletRequest request){
		//获取spring中的动态数据源bean
		DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource)MyWebContextFactory.getBean("dynamicDataSource");
		Map<Object, Object> dataSourceMap = new HashMap<Object, Object>();
		for(Monitor monitor : DynamicDataSourceContextHolder.getMonitors()){
			dataSourceMap.put(String.valueOf(monitor.getDbcode()), initDataSource(monitor));
		}
		Monitor monitor = new Monitor();
		monitor.setDbcode("fourth");
		monitor.setDbip("localhost");
		monitor.setDbname("websetup4");
		monitor.setDbport(3306);
		monitor.setDbuser("root");
		monitor.setDbpswd("root");
		DynamicDataSourceContextHolder.addMonitor(monitor);
		//添加第四个数据源
		dataSourceMap.put("fourth", initDataSource(monitor));
		//重置bean中多数据源配置
		dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
		//生效数据源配置
		dynamicRoutingDataSource.afterPropertiesSet();
		return YouXinResult.success("添加成功");
	}
	
	private DataSource initDataSource(Monitor monitor){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://"+ monitor.getDbip()+":"+ monitor.getDbport()+"/"+ monitor.getDbname()+"?characterEncoding=utf-8");
		dataSource.setUsername(monitor.getDbuser());
		dataSource.setPassword(monitor.getDbpswd());
		return dataSource;
	}
	
}
