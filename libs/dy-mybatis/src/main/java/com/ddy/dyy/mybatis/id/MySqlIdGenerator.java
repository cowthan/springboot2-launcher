package com.ddy.dyy.mybatis.id;

import java.io.StringReader;
import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;

/**
 * MySqlIdGenerator
 */
public class MySqlIdGenerator {
    private final DataSource dataSource;
    private boolean tableExists = false;
    private static final String TABLE_NAME = "t_id_generator";

    public MySqlIdGenerator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized long nextId(String biz) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String connectionUrl = connection.getMetaData().getURL();
            String dbName = getDbNameFromUrl(connectionUrl);

            SqlRunner sqlRunner = new SqlRunner(connection);
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setAutoCommit(true);
            scriptRunner.setStopOnError(true);

            //1、sql_version表创建
            synchronized (MySqlIdGenerator.class){
                if (!tableExists) {
                    Map<String, Object> r1 = sqlRunner.selectOne(getExistTableSql(dbName, TABLE_NAME));
                    if (r1 == null || "0".equals(String.valueOf(r1.get("R_COUNT")))) {
                        scriptRunner.runScript(new StringReader(getCreateTableSql(TABLE_NAME)));
                    }
                }
                tableExists = true;
            }


            //2、循环生成自增id
            long id = 0;
            int retryCount = 0;
            while (id == 0) {
                String sql = "select * from `%s` where biz = '%s'";
                sql = String.format(sql, TABLE_NAME, biz);
                Map<String, Object> bizRow = null;
                try {
                    bizRow = sqlRunner.selectOne(sql);
                }catch (Exception e){
                    bizRow = null;
                }
                if (bizRow == null) {
                    sql = "insert into %s (biz) values ('%s')";
                    sql = String.format(sql, TABLE_NAME, biz);
                    sqlRunner.insert(sql);
                    id = 1;
                } else {
                    long currentId = (long) bizRow.get("VAL");
                    sql = "update %s set val = val + 1 where biz = '%s' and val = %s";
                    sql = String.format(sql, TABLE_NAME, biz, currentId + "");
                    int effectRow = sqlRunner.update(sql);
                    if (effectRow == 1) {
                        id = currentId + 1;
                    }
                }
                retryCount++;
            }
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }

    private static String getCreateTableSql(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
                "\t`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "\t`biz` varchar(100) NOT NULL COMMENT '业务模块',\n" +
                "\t`val` bigint(20) NOT NULL default 1 COMMENT '自增序列号',\n" +
                "\tPRIMARY KEY (`id`), unique key `uk` (`biz`)\n" +
                ") COMMENT = '自增id生成器';";
        return String.format(sql, tableName);
    }


    private static String getExistTableSql(String dbName, String tableName) {
        String sql = "select count(*) R_COUNT from information_schema.tables "
                + " where table_name = '%s' and table_type ='BASE TABLE'"
                + " and table_schema = '%s'";
        sql = String.format(sql, tableName, dbName);
        return sql;
    }

    public static String getDbNameFromUrl(String url) {
        String[] parts = url.split("://");
        if (parts.length == 2) {
            String[] parts2 = parts[1].split("/");
            if (parts2.length > 1) {
                return parts2[1].split("\\?")[0];
            }
        }
        return null;
    }
}
