package com.ddy.dyy.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.ddy.dyy.mybatis.models.AssocArray;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 */
@Mapper
public interface SchemeMapper {

    @Select("${sql}")
    List<AssocArray> select(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int count(@Param("sql") String sql, @Param("map") AssocArray map);


    @Update("${sql}")
    int update(@Param("sql") String sql, @Param("map") Map<String, Object> map);

}
