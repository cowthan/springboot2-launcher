package org.danger.utils.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.danger.utils.AssocArray;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MyDb {

    private NamedParameterJdbcTemplate jdbcTemplate;


    public MyDb(DBConfig config2) {
        String uri = "jdbc:mysql://{host}:{port}/{dbname}" +
                "?characterEncoding=utf-8" +
                "&useUnicode=true" +
                "&allowMultiQueries=true" +
                "&serverTimezone=GMT%2b8" +
                "&useSSL=false";
        uri = uri.replace("{host}", config2.getHost());
        uri = uri.replace("{port}", config2.getPort() + "");
        uri = uri.replace("{dbname}", config2.getDbname());

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(uri);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 500);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        config.setAutoCommit(true);
        //池中最小空闲链接数量
        config.setMinimumIdle(10);
        //池中最大链接数量
        config.setMaximumPoolSize(50);
        config.setUsername(config2.getUsername());
        config.setPassword(config2.getPassword());

        HikariDataSource dataSource  = new HikariDataSource(config);
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        checkDbConnection();
    }

    public void checkDbConnection() {
//        String sql = "SELECT 1 FROM DUAL";
//        findRow(sql, null);
    }

    public int insert(String sql, AssocArray params){
        return jdbcTemplate.update(sql, params);
    }

    public long insertAndGetLastId(String sql, AssocArray params, String idColumnName){
        KeyHolder keyHolder=new GeneratedKeyHolder();
        SqlParameterSource sps = new MapSqlParameterSource();
        ((MapSqlParameterSource) sps).addValues(params);
        jdbcTemplate.update(sql, sps, keyHolder, new String[]{idColumnName});
        return keyHolder.getKey().longValue();
    }

//    public void insert(String table, List<Row> list){
//        for (int i = 0; i < Lang.count(list); i++) {
//            Object[] info = MyDbUtils.getInsertSql(table, list.get(i));
//            String sql = (String) info[0];
//            Params pm = (Params) info[1];
//
//        }
//    }

    public int update(String sql, AssocArray params){
        return jdbcTemplate.update(sql, params);
    }

    public int[] batchUpdate(String sql, AssocArray[] params){
        return jdbcTemplate.batchUpdate(sql,params);
    }

    public int delete(String sql, AssocArray params){
        return jdbcTemplate.update(sql, params);
    }

    public List<AssocArray> findAll(String sql){
        return findAll(sql, null);
    }
    public List<AssocArray> findAll(String sql, AssocArray params){
        List<AssocArray> rows = jdbcTemplate.query(sql, params, new GenericMapper());
        return rows;
    }

    public AssocArray findRow(String sql, AssocArray params){
        List<AssocArray> rows = jdbcTemplate.query(sql, params, new GenericMapper());
        if(rows != null && rows.size() > 0){
            return rows.get(0);
        }else{
            return null;
        }
    }

    public int findColumn(String sql, AssocArray params, int defaultValue){
        List<Integer> rows = jdbcTemplate.query(sql, params, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : defaultValue;
        }else{
            return defaultValue;
        }
    }

    public String findColumn(String sql, AssocArray params, String defaultValue){
        List<String> rows = jdbcTemplate.query(sql, params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : defaultValue;
        }else{
            return defaultValue;
        }
    }

    public Date findColumnDate(String sql, AssocArray params){
        List<Date> rows = jdbcTemplate.query(sql, params, new RowMapper<Date>() {
            @Override
            public Date mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getTimestamp(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : null;
        }else{
            return null;
        }
    }

    public double findColumn(String sql, AssocArray params, double defaultValue){
        List<Double> rows = jdbcTemplate.query(sql, params, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getDouble(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : defaultValue;
        }else{
            return defaultValue;
        }
    }

    public Date findColumn(String sql, AssocArray params, Date defaultValue){
        List<Timestamp> rows = jdbcTemplate.query(sql, params, new RowMapper<Timestamp>() {
            @Override
            public Timestamp mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getTimestamp(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : defaultValue;
        }else{
            return defaultValue;
        }
    }
}
