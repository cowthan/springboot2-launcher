package com.ddy.dyy.mybatis.sqlrunner;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class Connection2 {


    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                //ignore
            }
        }
    }

    public static Connection getConnection(DataSourceConfig dataSourceConfig) throws SQLException {
        DataSource dataSource = DataSource2.getDataSource(dataSourceConfig);
        Connection connection = dataSource.getConnection();
        return connection;
//        try {
//
//            String connectionUrl = connection.getMetaData().getURL();
//            String dbName = DataSource2.getDbNameFromUrl(connectionUrl);
//
//            SqlRunner sqlRunner = new SqlRunner(connection);
//            ScriptRunner scriptRunner = new ScriptRunner(connection);
//            scriptRunner.setAutoCommit(true);
//            scriptRunner.setStopOnError(true);
//
//            String sql = "select * from meta.cn_meta_satellite order by id asc";
//            List<Map<String, Object>> rows = sqlRunner.selectAll(sql);
//            System.out.println("查询结果：" + JsonUtils.toJsonPretty(rows));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (Exception e) {
//                    //ignore
//                }
//            }
//        }
    }

}
