package com.ddy.demo.x.config;

import java.util.Arrays;
import java.util.List;

import com.ddy.dyy.mybatis.ddl.ISqlScripts;
import org.springframework.stereotype.Component;

/**
 * .
 */
//@Component
public class DbMigrate implements ISqlScripts {

    @Override
    public List<String> getSqlScripts() {
//        String sql = "INSERT INTO `t_movie`";
        return Arrays.asList(
                "classpath:db_version/init.sql",
                "classpath:db_version/v1.sql",
//                "sql:" + sql,
                "classpath:db_version/v2.sql"
        );
    }
}