package com.secusoft.controller;

import io.swagger.annotations.Api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secusoft.common.YouXinResult;
import com.secusoft.model.DynamicDatabases;
import com.secusoft.service.MybatisTestService;

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
	private MybatisTestService mybatisTestService;
	
	@GetMapping(value = "/getDataBases")
	public YouXinResult<List<DynamicDatabases>> get(HttpServletRequest request,@RequestParam String dataSource){
		return YouXinResult.success(mybatisTestService.get(dataSource));
	}
	
	@GetMapping(value = "/testInsert")
	public YouXinResult<String> testInsert(HttpServletRequest request){
		mybatisTestService.testInsert("second", "test2");
		return YouXinResult.success("插入成功");
	}
	
	@GetMapping(value = "/testTransaction")
	public YouXinResult<String> testTransaction(HttpServletRequest request){
		mybatisTestService.testTransaction("third");
		return YouXinResult.success("success");
	}
	
}
