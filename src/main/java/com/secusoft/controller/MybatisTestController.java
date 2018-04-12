package com.secusoft.controller;

import io.swagger.annotations.Api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secusoft.common.YouXinResult;
import com.secusoft.mapper.DynamicDatabasesMapper;
import com.secusoft.model.DynamicDatabases;

/**
 * mybatis测试
 * @author yaojiacheng
 * 2018年4月11日
 */
@RestController
@RequestMapping("/mybatis")
@Api(value = "MybatisTestController", description = "mybatis测试")
public class MybatisTestController {
	
	@Autowired
	private DynamicDatabasesMapper dynamicDatabasesMapper;
	
	@GetMapping(value = "/getDataBases")
	public YouXinResult<List<DynamicDatabases>> get(HttpServletRequest request,@RequestParam String dataSource){
		return YouXinResult.success(dynamicDatabasesMapper.find(dataSource));
	}
	
	@GetMapping(value = "/testTransaction")
	@Transactional(rollbackFor=Exception.class)
	public YouXinResult<String> testTransaction(HttpServletRequest request) throws Exception{
		//一个事务内不能使用多个数据源
//		dynamicDatabasesMapper.insert("first", "test1");
//		dynamicDatabasesMapper.insert("second", "test2");
		dynamicDatabasesMapper.insert("third", "test3");
		dynamicDatabasesMapper.insert("third", "test4");
		throw new Exception();
	}
	
}
