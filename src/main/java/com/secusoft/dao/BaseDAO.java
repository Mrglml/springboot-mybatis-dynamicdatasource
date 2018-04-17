package com.secusoft.dao;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mysql.jdbc.JDBC4PreparedStatement;
import com.secusoft.model.Monitor;

/**
 * 数据库查询基础类
 * @author yaojiacheng
 * 2018年4月12日
 */
public class BaseDAO<T> {

    public Class<T> persistentClass;
    Connection conn = null;
    PreparedStatement sm = null;
    ResultSet rs = null;

    public BaseDAO() {
        ParameterizedType type = (ParameterizedType)getClass().getGenericSuperclass();
        persistentClass = (Class<T>)type.getActualTypeArguments()[0];
    }

    /**
     * 关闭连接
     * @param rs
     * @param sm
     * @param conn
     */
    public void closeConn(ResultSet rs,Statement sm,Connection conn){
         try {
             if(rs!=null) rs.close();
             if(sm!=null) sm.close();
             if(conn!=null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 过滤xss和sql注入脚本
     * @param value
     * @return
     */
    public String cleanXSS(String value){
        if(value==null){
            return null;
        }
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }

    /**
     * 获得连接
     * @return
     * @throws Exception
     */
    public Connection getConn(Monitor monitor)throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        String url ="jdbc:mysql://"+ monitor.getDbip()+":"+ monitor.getDbport()+"/"+ monitor.getDbname()+"?&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        return DriverManager.getConnection(url, monitor.getDbuser(), monitor.getDbpswd());
    }

    /**
     * 保存对象
     */
    public Boolean save(Monitor monitor,T entity){
        //SQL语句,insert into table name (
        String sql = "insert into " + entity.getClass().getSimpleName().toLowerCase() + "(";

        //获得带有字符串get的所有方法的对象
        List<Method> list = this.matchPojoMethods(entity,"get");

        Iterator<Method> iter = list.iterator();

        //拼接字段顺序 insert into table name(id,name,email,
        while(iter.hasNext()) {
            Method method = iter.next();
            sql += method.getName().substring(3).toLowerCase() + ",";
        }

        //去掉最后一个,符号insert insert into table name(id,name,email) values(
        sql = sql.substring(0, sql.lastIndexOf(",")) + ") values(";

        //拼装预编译SQL语句insert insert into table name(id,name,email) values(?,?,?,
        for(int j = 0; j < list.size(); j++) {
            sql += "?,";
        }

        //去掉SQL语句最后一个,符号insert insert into table name(id,name,email) values(?,?,?);
        sql = sql.substring(0, sql.lastIndexOf(",")) + ")";

        //到此SQL语句拼接完成,打印SQL语句
        System.out.println(sql);
        int affectedRows = 0;
        try {
            conn = this.getConn(monitor);
            //获得预编译对象的引用
            sm = conn.prepareStatement(sql);

            int i = 0;
            //把指向迭代器最后一行的指针移到第一行.
            iter = list.iterator();
            while(iter.hasNext()) {
                Method method = iter.next();
                //此初判断返回值的类型,因为存入数据库时有的字段值格式需要改变,比如String,SQL语句是'"+abc+"'
                if(method.getReturnType().getSimpleName().indexOf("String") != -1) {
                    sm.setString(++i, this.cleanXSS(this.getString(method, entity)));
                } else if(method.getReturnType().getSimpleName().indexOf("Date") != -1){
                    sm.setDate(++i, this.getDate(method, entity));
                } else if(method.getReturnType().getSimpleName().indexOf("Timestamp") != -1){
                    sm.setTimestamp(++i, this.getTimestamp(method, entity));
                } else if(method.getReturnType().getSimpleName().indexOf("InputStream") != -1) {
                    sm.setAsciiStream(++i, this.getBlob(method, entity),1440);
                } else {
                    sm.setInt(++i, this.getInt(method, entity));
                }
            }
            //执行
            affectedRows = sm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            try {
                this.closeConn(rs, sm, conn);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        Boolean flag = false;
        if(affectedRows>0){
            flag = true;
        }

        return flag;
    }

    /**
     * 保存并返回插入成功的id
     * @param monitor
     * @param entity
     * @return
     */
    public int  saveReturnId(Monitor monitor,T entity){

        int id =-1 ;
        //SQL语句,insert into table name (
        String sql = "insert into " + entity.getClass().getSimpleName().toLowerCase() + "(";

        //获得带有字符串get的所有方法的对象
        List<Method> list = this.matchPojoMethods(entity,"get");

        Iterator<Method> iter = list.iterator();

        //拼接字段顺序 insert into table name(id,name,email,
        while(iter.hasNext()) {
            Method method = iter.next();
            sql += method.getName().substring(3).toLowerCase() + ",";
        }

        //去掉最后一个,符号insert insert into table name(id,name,email) values(
        sql = sql.substring(0, sql.lastIndexOf(",")) + ") values(";

        //拼装预编译SQL语句insert insert into table name(id,name,email) values(?,?,?,
        for(int j = 0; j < list.size(); j++) {
            sql += "?,";
        }

        //去掉SQL语句最后一个,符号insert insert into table name(id,name,email) values(?,?,?);
        sql = sql.substring(0, sql.lastIndexOf(",")) + ")";

        //到此SQL语句拼接完成,打印SQL语句
        System.out.println(sql);
        int affectedRows = 0;
        try {
            conn = this.getConn(monitor);
            //获得预编译对象的引用
            sm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int i = 0;
            //把指向迭代器最后一行的指针移到第一行.
            iter = list.iterator();
            while(iter.hasNext()) {
                Method method = iter.next();
                //此初判断返回值的类型,因为存入数据库时有的字段值格式需要改变,比如String,SQL语句是'"+abc+"'
                if(method.getReturnType().getSimpleName().indexOf("String") != -1) {
                    sm.setString(++i, this.cleanXSS(this.getString(method, entity)));
                } else if(method.getReturnType().getSimpleName().indexOf("Date") != -1){
                    sm.setDate(++i, this.getDate(method, entity));
                } else if(method.getReturnType().getSimpleName().indexOf("Timestamp") != -1){
                    sm.setTimestamp(++i, this.getTimestamp(method, entity));
                } else if(method.getReturnType().getSimpleName().indexOf("InputStream") != -1) {
                    sm.setAsciiStream(++i, this.getBlob(method, entity),1440);
                } else {
                    sm.setInt(++i, this.getInt(method, entity));
                }
            }
            //执行
            System.out.print(((JDBC4PreparedStatement)sm).asSql());
            affectedRows = sm.executeUpdate();
            rs = sm.getGeneratedKeys();
            if(rs.next())
            {
                id = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            try {
                this.closeConn(rs, sm, conn);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return id;
    }
    /**
     * 修改对象
     */
    public Boolean update(Monitor monitor, T entity,String idColumnName,Integer idValue){
        String sql = "update " + entity.getClass().getSimpleName().toLowerCase() + " set ";

        //获得该类所有get方法对象集合
        List<Method> list = this.matchPojoMethods(entity,"get");

        //临时Method对象,负责迭代时装method对象.
        Method tempMethod = null;

        Iterator<Method> iter = list.iterator();

        //把迭代指针移到第一位
        iter = list.iterator();
        while(iter.hasNext()) {
            tempMethod = iter.next();
            sql += tempMethod.getName().substring(3).toLowerCase() + "= ?,";
        }

        //去掉最后一个,符号
        sql = sql.substring(0,sql.lastIndexOf(","));

        //添加条件
        sql += " where " + idColumnName+ " = ?";

        //SQL拼接完成,打印SQL语句
        System.out.println(sql);
        int affectedRows = 0;
        try {
            conn = this.getConn(monitor);
            sm = this.conn.prepareStatement(sql);

            int i = 0;
            iter = list.iterator();
            while(iter.hasNext()) {
                Method method = iter.next();
                //此初判断返回值的类型,因为存入数据库时有的字段值格式需要改变,比如String,SQL语句是'"+abc+"'
                if(method.getReturnType().getSimpleName().indexOf("String") != -1) {
                    sm.setString(++i, this.cleanXSS(this.getString(method, entity)));
                } else if(method.getReturnType().getSimpleName().indexOf("Date") != -1){
                    sm.setDate(++i, this.getDate(method, entity));
                } else if(method.getReturnType().getSimpleName().indexOf("InputStream") != -1) {
                    sm.setAsciiStream(++i, this.getBlob(method, entity),1440);
                } else {
                    sm.setInt(++i, this.getInt(method, entity));
                }
            }

            //为Id字段添加值
            sm.setInt(++i, idValue);

            //执行SQL语句
            affectedRows = sm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            try {
                this.closeConn(rs, sm, conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Boolean flag = false;
        if(affectedRows>0){
            flag = true;
        }

        return flag;
    }

    /**
     * 增加,删除,修改
     * @param sql
     * @param args
     * @return
     */
    public boolean executeUpdate(Monitor monitor, String sql, Object[] args){
        boolean flag=false;
        try {
            conn=this.getConn(monitor);
            sm=conn.prepareStatement(sql);
            if(conn != null){
                for (int i = 0; i < args.length; i++) {
                    sm.setObject(i+1, args[i]);
                }
            }
            if(sm.executeUpdate() > 0){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                this.closeConn(null, sm, conn);
            } catch (Exception e) {
                
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 查询记录数
     * @param monitor
     * @param sql
     * @param args
     * @return
     */
    public int queryRowCount(Monitor monitor, String sql, Object[] args){
        int rowCount = 0;
        try {
            conn = this.getConn(monitor);
            sm = conn.prepareStatement(sql);
            if(args != null){
                for (int i = 0; i < args.length; i++) {
                    sm.setObject(i+1, args[i]);
                }
            }

            rs=sm.executeQuery();
            if(rs.next())
            {
                rowCount = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                this.closeConn(rs, sm, conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rowCount;
    }


    /**
     * 查询方法
     * @param sql
     * @param args
     * @return
     */
    public List executeQuery(Monitor monitor, String sql, Object[] args){
        List list=new ArrayList();

        try {
            conn = this.getConn(monitor);
            sm = conn.prepareStatement(sql);
            if(args != null){
                for (int i = 0; i < args.length; i++) {
                	sm.setObject(i+1, args[i]);
                }
            }

            rs=sm.executeQuery();
            while(rs.next()){
                //通过反射得到一个对象
                list.add(this.getObj(rs));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                this.closeConn(rs, sm, conn);
            } catch (Exception e) {
                
                e.printStackTrace();
            }
        }
        return list;
    }

    /* 用于将rs查询结果封装为List<Map<String, Object>>对象
     *
     * @param rs数据库查询结果
     * @return 返回list map封装后的结果
     */
    public List<Map<String, Object>> executeQueryForList(Monitor monitor, String sql, Object[] args) {
        // 新建一个map list集合用于存放多条查询记录
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            conn = this.getConn(monitor);
            sm = conn.prepareStatement(sql);
            if(args != null){
                for (int i = 0; i < args.length; i++) {
                    sm.setObject(i+1, args[i]);
                }
            }

            rs=sm.executeQuery();
            ResultSetMetaData md = rs.getMetaData();// 结果集(rs)的结构信息，比如字段数、字段名等。
            int columnCount = md.getColumnCount();// 取得查询出来的字段个数
            while (rs.next()) {// 迭代rs
                // 新建一个map集合 将查询出内容按照字段名：值 的键值对形式存储在map集合中
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {// 循环所有查询出字段
                    rowData.put(md.getColumnLabel(i), rs.getObject(i));
                    // getColumnName(i) 获取第i个列名
                    // getObject(i) 获取第i个对象的值
                }
                list.add(rowData);// 将map放入list集合中
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {// 关闭连接
            try {
                this.closeConn(rs, sm, conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }



    /*
     * 用于将rs查询结果封装为Map<String, Object>对象（适合于只有一条查询记录）
     *
     * @param rs数据库查询结果
     * @return 返回map封装后 字段名：值 的键值对结果
     */
    public Map<String, Object> executeQueryForMap(Monitor monitor, String sql, Object[] args) {
        Map<String, Object> map = new TreeMap<String, Object>();
        try {
            conn = this.getConn(monitor);
            sm = conn.prepareStatement(sql);
            if(args != null){
                for (int i = 0; i < args.length; i++) {
                    sm.setObject(i+1, args[i]);
                }
            }

            rs=sm.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    map.put(md.getColumnName(i), rs.getObject(i));
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.closeConn(rs, sm, conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 根据数据库返回结果通过反射自动封装属性
     * @param rs
     * @return
     * @throws Exception
     */
    private T getObj(ResultSet rs)throws Exception{

        //通过子类的构造函数,获得参数化类型的具体类型.比如BaseDAO<T>也就是获得T的具体类型
        T entity = persistentClass.newInstance();
        List<Method> list = this.matchPojoMethods(entity, "set");
        Iterator<Method> iter = list.iterator();
        //封装
        while(iter.hasNext()) {
            Method method = iter.next();
            if(method.getParameterTypes()[0].getSimpleName().indexOf("String") != -1) {
                //由于list集合中,method对象取出的方法顺序与数据库字段顺序不一致(比如:list的第一个方法是setDate,而数据库按顺序取的是"123"值)
                //所以数据库字段采用名字对应的方式取.
                this.setString(method, entity, rs.getString(method.getName().substring(3).toLowerCase()));
            } else if(method.getParameterTypes()[0].getSimpleName().indexOf("Date") != -1){
                this.setDate(method, entity, rs.getDate(method.getName().substring(3).toLowerCase()));
            } else if(method.getParameterTypes()[0].getSimpleName().indexOf("Timestamp") != -1){
                this.setTimestamp(method, entity, rs.getTimestamp(method.getName().substring(3).toLowerCase()));
            } else if(method.getParameterTypes()[0].getSimpleName().indexOf("InputStream") != -1) {
                this.setBlob(method, entity, rs.getBlob(method.getName().substring(3).toLowerCase()).getBinaryStream());
            } else {
                this.setInt(method, entity, rs.getInt(method.getName().substring(3).toLowerCase()));
            }
        }

        return entity;
    }

    /**
     * 过滤当前Pojo类所有带传入字符串的Method对象,返回List集合.
     */
    public List<Method> matchPojoMethods(T entity,String methodName) {
        //获得当前Pojo所有方法对象
        Method[] methods = entity.getClass().getDeclaredMethods();

        //List容器存放所有带get字符串的Method对象
        List<Method> list = new ArrayList<Method>();

        //过滤当前Pojo类所有带get字符串的Method对象,存入List容器
        for(int index = 0; index < methods.length; index++) {
            if(methods[index].getName().indexOf(methodName) != -1) {
                list.add(methods[index]);
            }
        }
        return list;
    }
    /**
     * 方法返回类型为int或Integer类型时,返回的SQL语句值.对应get
     */
    public Integer getInt(Method method, T entity) throws Exception{
        return (Integer)method.invoke(entity, new Object[]{});
    }

    /**
     * 方法返回类型为String时,返回的SQL语句拼装值.比如'abc',对应get
     */
    public String getString(Method method, T entity) throws Exception{
        return (String)method.invoke(entity, new Object[]{});
    }

    /**
     * 方法返回类型为Blob时,返回的SQL语句拼装值.对应get
     */
    public InputStream getBlob(Method method, T entity) throws Exception{
        return (InputStream)method.invoke(entity, new Object[]{});
    }


    /**
     * 方法返回类型为Date时,返回的SQL语句拼装值,对应get
     */
    public Date getDate(Method method, T entity) throws Exception{
        return (Date)method.invoke(entity, new Object[]{});
    }

    /**
     * 方法返回类型为Timestamp时,返回的SQL语句拼装值,对应get
     */
    public Timestamp getTimestamp(Method method, T entity) throws Exception{
        return (Timestamp)method.invoke(entity, new Object[]{});
    }

    /**
     * 参数类型为Integer或int时,为entity字段设置参数,对应set
     */
    public Integer setInt(Method method, T entity, Integer arg) throws Exception{
        return (Integer)method.invoke(entity, new Object[]{arg});
    }

    /**
     * 参数类型为String时,为entity字段设置参数,对应set
     */
    public String setString(Method method, T entity, String arg) throws Exception{
        return (String)method.invoke(entity, new Object[]{arg});
    }

    /**
     * 参数类型为InputStream时,为entity字段设置参数,对应set
     */
    public InputStream setBlob(Method method, T entity, InputStream arg) throws Exception{
        return (InputStream)method.invoke(entity, new Object[]{arg});
    }


    /**
     * 参数类型为Date时,为entity字段设置参数,对应set
     */
    public Date setDate(Method method, T entity, Date arg) throws Exception{
        return (Date)method.invoke(entity, new Object[]{arg});
    }

    /**
     * 参数类型为Date时,为entity字段设置参数,对应set
     */
    public Timestamp setTimestamp(Method method, T entity, Timestamp arg) throws Exception{
        return (Timestamp)method.invoke(entity, new Object[]{arg});
    }
}