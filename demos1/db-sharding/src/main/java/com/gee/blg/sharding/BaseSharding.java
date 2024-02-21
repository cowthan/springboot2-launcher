package com.gee.blg.sharding;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

/**
 * 分表工具
 */
@Slf4j
public abstract class BaseSharding<T extends Comparable<?>> implements PreciseShardingAlgorithm<T>, RangeShardingAlgorithm<T> {

    private static JdbcTemplate jdbcTemplate;
    private static final HashSet<String> tableNameCache = new HashSet<>();

    public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        BaseSharding.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 判断 分表获取的表名是否存在 不存在则自动建表
     *
     * @param logicTableName  逻辑表名(表头)
     * @param resultTableName 真实表名
     * @return 确认存在于数据库中的真实表名
     */
    public String shardingTablesCheckAndCreatAndReturn(String logicTableName, String resultTableName) {

        synchronized (logicTableName.intern()) {
            // 缓存中有此表 返回
            if (shardingTablesExistsCheck(resultTableName)) {
                return resultTableName;
            }

            // 缓存中无此表 建表 并添加缓存

            // 查询SHOW CREATE TABLE，方式1，返回null
//            CreateTableSql createTableSql = commonMapper.selectTableCreateSql(logicTableName);
//            String sql = createTableSql.getCreateTable();

            // 查询SHOW CREATE TABLE，方式2，返回null
//            Map<String, Object> map = jdbcTemplate.queryForMap("SHOW CREATE TABLE " + logicTableName);

            // 查询SHOW CREATE TABLE，方式3，这样才行
            Map<String, Object> map = jdbcTemplate.execute("SHOW CREATE TABLE " + logicTableName, new PreparedStatementCallback<Map<String, Object>>() {
                @Override
                public Map<String, Object> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ResultSet resultSet = ps.executeQuery();
                    String table = resultSet.getString("Table");
                    String table2 = resultSet.getString("Create Table");
                    Map<String, Object> ret = new HashMap<>();
                    ret.put("Table", table);
                    ret.put("Create Table", table2);
                    return ret;
                }
            });

            String sql = map.get("Create Table").toString();
            sql = sql.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS");
            sql = sql.replace(logicTableName, resultTableName);
            jdbcTemplate.execute(sql);
            tableNameCache.add(resultTableName);
        }

        return resultTableName;
    }

    /**
     * 判断表是否存在于缓存中
     *
     * @param resultTableName 表名
     * @return 是否存在于缓存中
     */
    public boolean shardingTablesExistsCheck(String resultTableName) {
        return tableNameCache.contains(resultTableName);
    }

    /**
     * 缓存重载方法
     *
     * @param schemaName 待加载表名所属数据库名
     */
    public static void tableNameCacheReload(String schemaName) {
        // 读取数据库中所有表名
        /*
        SELECT TABLES.TABLE_NAME
        FROM information_schema.TABLES
        WHERE TABLES.TABLE_SCHEMA = 'db name'
         */
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT TABLES.TABLE_NAME FROM information_schema.TABLES WHERE TABLES.TABLE_SCHEMA = '"
                + schemaName + "'");
        List<String> tableNameList = new ArrayList<>();
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                tableNameList.add(row.get("TABLE_NAME").toString());
            }
        }

        // 删除旧的缓存(如果存在)
        BaseSharding.tableNameCache.clear();
        // 写入新的缓存
        BaseSharding.tableNameCache.addAll(tableNameList);
    }

}

