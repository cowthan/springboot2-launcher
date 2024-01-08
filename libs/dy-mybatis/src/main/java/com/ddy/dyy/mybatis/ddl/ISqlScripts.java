package com.ddy.dyy.mybatis.ddl;

import java.util.List;

/**
 * sql脚本定义，可以直接是sql语句，可以是指定sql文件路径
 */
public interface ISqlScripts {

    List<String> getSqlScripts();

}
