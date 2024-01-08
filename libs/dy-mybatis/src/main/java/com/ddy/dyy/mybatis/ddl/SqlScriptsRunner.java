package com.ddy.dyy.mybatis.ddl;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * SqlScriptsRunner：用于执行sql脚本，更新数据库版本
 */
@Slf4j
@Component
public class SqlScriptsRunner {

    @Autowired
    private ISqlScripts sqlScripts;

    @Autowired(required = false)
    private DataSource dataSource;

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent e) {
        if(dataSource == null){
            return;
        }
        run();
    }

    public void run() {

        log.warn("数据库初始化开始...");
        try {
            Connection connection = dataSource.getConnection();
            String connectionUrl = connection.getMetaData().getURL();
            String dbName = getDbNameFromUrl(connectionUrl);
            String tableName = "db_version";
            SqlRunner sqlRunner = new SqlRunner(connection);
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setAutoCommit(true);
            scriptRunner.setStopOnError(true);

            //1、sql_version表创建
            Map<String, Object> r1 = sqlRunner.selectOne(getExistTableSql(dbName, tableName));
            if (r1 == null || "0".equals(String.valueOf(r1.get("R_COUNT")))) {
                scriptRunner.runScript(new StringReader(getCreateTableSql(tableName)));
            }

            //2、挨个执行sql脚本
            List<String> scripts = sqlScripts.getSqlScripts();
            for (int i = 0; i < scripts.size(); i++) {
                String script = scripts.get(i);
                String md5 = DigestUtils.md5Hex(script);
                List<Map<String, Object>> r2 = sqlRunner.selectAll(getCheckRepeatSql(tableName, md5, "sql"));
                if (r2 == null || r2.size() == 0) {
                    // 执行脚本
                    log.warn("数据库初始化，执行：{}", script);
                    runScript(scriptRunner, script);
                    // 更新版本
                    String version = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String insertSql = getInsertSql(tableName, md5, "sql", version);
                    sqlRunner.insert(insertSql, script);
                }
            }
        } catch (Exception e) {
            log.error("数据库初始化错误", e);
        }

        log.warn("数据库初始化结束...");

    }

    private static void runScript(ScriptRunner scriptRunner, String script) throws IOException, SQLException {
        String r = script.substring(script.indexOf(":") + 1).trim();
        if (script.startsWith("file:")) {
            // 外部文件
            scriptRunner.runScript(new FileReader(r));
        } else if (script.startsWith("classpath:")) {
            // 资源文件
            scriptRunner.runScript(new InputStreamReader((new ClassPathResource(r)).getInputStream()));
        } else if (script.startsWith("sql:")) {
            // sql脚本
            scriptRunner.runScript(new StringReader(r));
        }
    }

    private static String getExistTableSql(String dbName, String tableName) {
        String sql = "select count(*) R_COUNT from information_schema.tables "
                + " where table_name = '%s' and table_type ='BASE TABLE'"
                + " and table_schema = '%s'";
        sql = String.format(sql, tableName, dbName);
        return sql;
    }

    private static String getCreateTableSql(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
                "\t`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "\t`script_md5` varchar(100) NOT NULL COMMENT 'id',\n" +
                "\t`script` text NOT NULL COMMENT '脚本',\n" +
                "\t`type` varchar(30) NOT NULL COMMENT '类型',\n" +
                "\t`version` varchar(30) NOT NULL COMMENT '版本',\n" +
                "\tPRIMARY KEY (`id`), unique key `uk` (`script_md5`)\n" +
                ") COMMENT = '数据库版本';";
        return String.format(sql, tableName);
    }

    private static String getCheckRepeatSql(String tableName, String md5, String type) {
        String sql = "select 1 from %s where script_md5='%s' AND type='%s'";
        return String.format(sql, tableName, md5, type);
    }

    private static String getInsertSql(String tableName, String md5, String type, String version) {
        String sql = "insert into %s(script_md5, script, type, version) values ('%s',?,'%s', '%s')";
        return String.format(sql, tableName, md5, type, version);
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
