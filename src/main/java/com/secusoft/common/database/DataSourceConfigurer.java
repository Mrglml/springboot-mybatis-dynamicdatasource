package com.secusoft.common.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.secusoft.model.Monitor;

@Configuration
@EnableTransactionManagement(order = 2)//由于引入多数据源，所以让spring声明式事务的aop要在多数据源切换的aop的后面
public class DataSourceConfigurer {
	
	@Value("${spring.profiles.active}")
	private String active;
	
	private Properties getProperties(){
		//读取配置文件
		String separator = System.getProperty("file.separator");
		String dbConfigPath = System.getProperty("user.dir")+separator+"config"+separator+"application-"+active+".properties";
		Properties properties = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					new File(dbConfigPath)));
			properties.load(in);
		} catch (IOException e) {		
			e.printStackTrace();
		}
		return properties;
	}
	
	private DataSource initDataSource(Properties properties, String dataSourcName){
		DruidDataSource dataSource = new DruidDataSource();
		dataSourcName = "spring.datasource."+dataSourcName+".";
		dataSource.setDriverClassName(properties.getProperty(dataSourcName+"driver-class-name"));
		dataSource.setUrl(properties.getProperty(dataSourcName+"url"));
		dataSource.setUsername(properties.getProperty(dataSourcName+"username"));
		dataSource.setPassword(properties.getProperty(dataSourcName+"password"));
		return dataSource;
	}
	
	private DataSource initDataSource(Monitor monitor){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://"+ monitor.getDbip()+":"+ monitor.getDbport()+"/"+ monitor.getDbname()+"?characterEncoding=utf-8");
		dataSource.setUsername(monitor.getDbuser());
		dataSource.setPassword(monitor.getDbpswd());
		return dataSource;
	}

//    @Bean("first")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.first")
//    public DataSource first() {
//        return new DruidDataSource();
//    }
//
//    @Bean("second")
//    @ConfigurationProperties(prefix = "spring.datasource.second")
//    public DataSource second() {
//        return new DruidDataSource();
//    }

    /**
     * Dynamic data source.
     *
     * @return the data source
     */
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
    	DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
    	Map<Object, Object> dataSourceMap = new HashMap<Object, Object>();
    	if("dev".equals(active)){
    		//已通过主数据库查询到其他数据源的配置
    		for(Monitor monitor : DynamicDataSourceContextHolder.getMonitors()){
    			dataSourceMap.put(monitor.getDbcode(), initDataSource(monitor));
    		}
    	}else{
    		//通过读取配置文件获取数据源配置
    		Properties properties = getProperties();
    		String datasources = properties.getProperty("datasources");
    		if(datasources!=null && !datasources.isEmpty()){
    			JSONArray ja = JSON.parseArray(datasources);
    			for(Object ob : ja){
    				dataSourceMap.put((String)ob, initDataSource(properties,(String)ob));
    			}
    		}
    	}
        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSourceMap.get("first"));
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
        return dynamicRoutingDataSource;
    }

    /**
     * Sql session factory bean.
     * Here to config datasource for SqlSessionFactory
     * <p>
     * You need to add @{@code @ConfigurationProperties(prefix = "mybatis")}, if you are using *.xml file,
     * the {@code 'mybatis.type-aliases-package'} and {@code 'mybatis.mapper-locations'} should be set in
     * {@code 'application.properties'} file, or there will appear invalid bond statement exception
     *
     * @return the sql session factory bean
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // Here is very important, if don't config this, will can't switch datasource
        // put all datasource into SqlSessionFactoryBean, then will autoconfig SqlSessionFactory
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBean;
    }

    /**
     * Transaction manager platform transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}

