package com.secusoft.model;

/**
 * 数据源表
 * @author yaojiacheng
 * 2018年4月12日
 */
public class Monitor{
	private int id;
	private String dbcode;
	private String dbname;
	private String dbuser;
	private String dbpswd;
	private String dbip;
	private int dbport;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDbcode() {
		return dbcode;
	}
	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getDbuser() {
		return dbuser;
	}
	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}
	public String getDbpswd() {
		return dbpswd;
	}
	public void setDbpswd(String dbpswd) {
		this.dbpswd = dbpswd;
	}
	public String getDbip() {
		return dbip;
	}
	public void setDbip(String dbip) {
		this.dbip = dbip;
	}
	public int getDbport() {
		return dbport;
	}
	public void setDbport(int dbport) {
		this.dbport = dbport;
	}
	@Override
	public String toString() {
		return "Monitor [id=" + id + ", dbcode=" + dbcode + ", dbname="
				+ dbname + ", dbuser=" + dbuser + ", dbpswd=" + dbpswd
				+ ", dbip=" + dbip + ", dbport=" + dbport + "]";
	}
}

