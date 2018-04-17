package com.secusoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.secusoft.exception.YouXinException;
import com.secusoft.mapper.DynamicDatabasesMapper;
import com.secusoft.model.DynamicDatabases;
import com.secusoft.service.MybatisTestService;

@Service
public class MybatisTestServiceImpl implements MybatisTestService {
	
	@Autowired
	private DynamicDatabasesMapper dynamicDatabasesMapper;

	@Override
	public List<DynamicDatabases> get(String dataSource) {
		return dynamicDatabasesMapper.find();
	}

	@Override
	@Transactional
	public void testInsert(String dataSource, String databaseName) {
		dynamicDatabasesMapper.insert(databaseName);
	}

	@Override
	@Transactional
	public void testTransaction(String dataSource) {
		dynamicDatabasesMapper.insert("testTransaction1");
		dynamicDatabasesMapper.insert("testTransaction2");
		throw new YouXinException("testTransaction");
	}

}
