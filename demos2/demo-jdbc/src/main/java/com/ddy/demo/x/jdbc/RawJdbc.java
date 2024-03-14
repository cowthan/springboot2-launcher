package com.ddy.demo.x.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ddy.dyy.web.lang.JsonUtils;
import com.ddy.dyy.web.lang.Lang;

public class RawJdbc {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${DB_NAME}" +
                "?useSSL=false" +
                "&useUnicode=true" +
                "&characterEncoding=utf8" +
                "&serverTimezone=GMT%2b8" +
                "&allowPublicKeyRetrieval=True" +
                "&allowMultiQueries=true" +
                "&useSSL=false" +
                "&serverTimezone=GMT%2B8";
        url = url.replace("${MYSQL_HOST}", "172.16.101.96");
        url = url.replace("${MYSQL_PORT}", "3306");
        url = url.replace("${DB_NAME}", "cn-chat");
        String username = "root";
        String password = "Pwd_2018";
        Connection conn = getConnection(url, username, password);
        Long insertId = insert(conn, "id");
        if (insertId == null) {
            System.out.println("插入失败");
        } else {
            System.out.println("新插入行id = " + insertId);

            Map<String, Object> row = selectById(conn, insertId);
            System.out.println("查询结果：" + JsonUtils.toJson(row));

            boolean deleted = deleteById(conn, insertId);
            System.out.println("删除结果：" + deleted);
        }

        close(conn, null, null);
    }

    public static Connection getConnection(String url, String username, String password) {
        Connection conn = null;
        try {
            // 2，加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 3.连接数据库
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("驱动类找不到");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接失败");
        }

        return conn;
    }


    public static void close(Connection conn, Statement stm, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Long insert(Connection conn, String autoIncrementIdName) throws SQLException {
        PreparedStatement preparedStatement = null;
        boolean hasAutoIncrement = autoIncrementIdName != null && !autoIncrementIdName.isEmpty();
        try {
            // 2.sql 语句，占位符
//        String sql = "INSERT INTO cc_msg_2024_03_10 (user_id, terminal_id, seq, msg_id, payload, chat_time, direction, status)\n" +
//                "VALUES(104, 37, 134, '${msg_id}', '${payload}', '${chat_time}', 'IN', 'ALREADY_RECEIVED');\n";
//        sql = sql.replace("${msg_id}", UUID.randomUUID().toString().replace("-", ""));
//        sql = sql.replace("${payload}", "消息内容-" + Lang.getNow());
//        sql = sql.replace("${chat_time}", Lang.getNow(true));

            String sql = "INSERT INTO cc_msg (user_id, terminal_id, seq, msg_id, payload, chat_time, direction, status)\n" +
                    "VALUES(104, 37, 134, ?, ?, ?, 'IN', 'ALREADY_RECEIVED');\n";

            // 3.获取sql语句对象
            if (hasAutoIncrement) {
                preparedStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            } else {
                preparedStatement = conn.prepareStatement(sql);

            }
            // 4.填入字段内容
            // 第一个占位符字段，string类型
            preparedStatement.setString(1, UUID.randomUUID().toString().replace("-", ""));
            // 第二个占位符字段，int类型
            preparedStatement.setString(2, "消息内容-" + Lang.getNow());
            preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
            // 5.执行sql
            int rows = preparedStatement.executeUpdate();
            if (rows == 1) {
                if (hasAutoIncrement) {
                    // 获取自增ID
                    long generatedId = -1;
                    try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                        if (rs.next()) {
                            generatedId = rs.getLong(1);
                        }
                    }
                    return generatedId;
                } else {
                    return 0L;
                }
            } else {
                return null;
            }
        } finally {
            close(null, preparedStatement, null);
        }
    }

//    public void insertBatch() throws SQLException {
//        Connection conn = null;
//        conn = Util.getConn();
//        // 关闭自动提交
//        conn.setAutoCommit(false);
//
//        String sql = "INSERT INTO example (name, age) VALUES (?, ?)";
//        PreparedStatement statement = conn.prepareStatement(sql);
//
//        for (int i = 0; i < 100; i++) {
//            statement.setString(1, "Galaxy" + i);
//            statement.setInt(2, new Random().nextInt(18, 65));
//
//            statement.addBatch();
//            if (i % 10 == 0) {
//                statement.executeBatch();
//            }
//        }
//        statement.executeBatch();
//        conn.commit();
//
//        statement.close();
//        conn.close();
//    }

    public static boolean deleteById(Connection conn, long id) throws SQLException {
        String sql = "DELETE FROM cc_msg WHERE id=?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        int rows = preparedStatement.executeUpdate();
        close(null, preparedStatement, null);
        return rows == 1;
    }

//    public void update() throws SQLException {
//        Connection conn = null;
//        // 1.获取conn
//        conn = Util.getConn();
//        // 2.sql语句
//        String sql = "UPDATE example SET age=? WHERE id=?";
//        // 3.获取prepareStatement
//        PreparedStatement preparedStatement = conn.prepareStatement(sql);
//        // 4.根据占位符填入值
//        preparedStatement.setInt(1, 66);
//        preparedStatement.setInt(2, 2);
//        // 5.执行语句
//        int result = preparedStatement.executeUpdate();
//        System.out.println("影响行数：" + result);
//
//        Util.close(conn, preparedStatement, null);
//    }

    public static Map<String, Object> selectById(Connection conn, long id) throws SQLException{
        // 2.sql语句
        String sql = "SELECT * FROM cc_msg WHERE id=?";
        // 3.获取对象
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setLong(1,id);
        // 4.执行语句
        ResultSet rs = preparedStatement.executeQuery();
        // 5.处理结果，多条结果用while，单条用if
        Map<String, Object> ret = new HashMap<>();
        ResultSetMetaData rsmd = rs.getMetaData() ;
        int columnCount = rsmd.getColumnCount();
        if (rs.next()) {
            // 通过字段名获取对象值
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                Object value = rs.getObject(i);
                ret.put(columnName, value);
            }
        }

        close(null, preparedStatement, rs);
        return ret;
    }

//    public void test() throws SQLException {
//        Connection conn = Util.getConn();
//        PreparedStatement smt = null;
//        try {
//            // 1.开启事务
//            conn.setAutoCommit(false);
//
//            String sql = "UPDATE example SET age=? WHERE name=?";
//            smt = conn.prepareStatement(sql);
//
//            smt.setInt(1, 55);
//            smt.setString(2, "Greg");
//            smt.execute();
//            // 2.提交事务
//            conn.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // 3.回滚事务
//            conn.rollback();
//        }
//
//        smt.close();
//        conn.close();
//    }

//    public void testDDL() throws SQLException {
//        Connection conn = Util.getConn();
//
//        Statement statement = conn.createStatement();
//
//        // create table
//        String tableSql = "CREATE TABLE test (id int auto_increment primary key, name varchar(20), age int)";
//
//        // exec
//        int res = statement.executeUpdate(tableSql);
//        System.out.println("建表成功：" + res);
//    }


}
