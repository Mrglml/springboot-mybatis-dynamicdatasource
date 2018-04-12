package com.secusoft.dao;

import java.util.List;

import com.secusoft.model.Monitor;

public class MonitorDao extends BaseDAO<Monitor>{
	
	/**
	 * 通过jdbc查询主数据库中配置的所有数据源
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Monitor> selectMonitors(){
		Monitor monitor = new Monitor();
		monitor.setDbip("localhost");
		monitor.setDbname("websetup_global");
		monitor.setDbport(3306);
		monitor.setDbuser("root");
		monitor.setDbpswd("root");
        String sql = "select * from monitor";
        List<Monitor> monitors = super.executeQuery(monitor,sql,new Object[]{});
        return monitors;
    }

}
