package com.secusoft.service;

import java.util.List;

import com.secusoft.model.DynamicDatabases;

/**
 * aop中通过service方法的第一个参数来确定本次操作的数据源
 * @author yaojiacheng
 * 2018年4月17日
 */
public interface MybatisTestService {
	
	List<DynamicDatabases> get(String dataSource);
	
	void testInsert(String dataSource,String databaseName);
	
	void testTransaction(String dataSource);
	
}
