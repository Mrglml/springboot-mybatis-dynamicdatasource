package com.secusoft.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.secusoft.model.DynamicDatabases;

@Mapper
public interface DynamicDatabasesMapper {

    @Select("SELECT * FROM dynamicdatabases")
    List<DynamicDatabases> find();

    @Insert("INSERT INTO dynamicdatabases(databaseName) VALUES(#{databaseName})")
    int insert(@Param("databaseName") String databaseName);

}
