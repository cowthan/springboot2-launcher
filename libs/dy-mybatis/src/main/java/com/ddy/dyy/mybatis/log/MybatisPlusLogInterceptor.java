package com.ddy.dyy.mybatis.log;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.ddy.dyy.mybatis.DyMybatisPlusProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mybatis日志美化，不支持oracle
 */
@Slf4j
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class MybatisPlusLogInterceptor extends BaseMybatisLogInterceptor {

    AtomicLong counter = new AtomicLong();

    protected DyMybatisPlusProperties properties;

    @Autowired
    public MybatisPlusLogInterceptor(DyMybatisPlusProperties properties) {
        this.properties = properties;
    }

    /**
     * shouldLog
     *
     * @param sql sql
     * @return result
     */
    @Override
    protected boolean shouldLog(String sql) {
        if (properties.isLogEnable()) {
            if (properties.getIgnoreKeywords() != null) {
                for (String k : properties.getIgnoreKeywords().split(",")) {
                    if (!"".equals(k.trim()) && sql.contains(k.trim())) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param seq     .
     * @param sql     .
     * @param sqlCost .
     * @param size    .
     */
    protected void log(long seq, String sql, long sqlCost, int size) {
        String txt = "SQL - {}: [{}] 耗时[{}ms] 结果集[{}]";
        if (properties.getLogLevel().equals("info")) {
            log.info(txt, seq, sql, sqlCost, size);
        } else if (properties.getLogLevel().equals("warn")) {
            log.warn(txt, seq, sql, sqlCost, size);
        } else if (properties.getLogLevel().equals("debug")) {
            log.debug(txt, seq, sql, sqlCost, size);
        }
    }

    /**
     * 把对象或map展开，带前缀，可嵌套
     *
     * @param prefix .
     * @param value  .
     * @return .
     */
    @Override
    protected Map obj2map(String prefix, Object value) {
        Map map = new HashMap();
        if (value instanceof AbstractWrapper) {
            //处理 QueryWrapper 和 LambdaQueryWrapper
            AbstractWrapper lq = (AbstractWrapper) value;
            Map map3 = lq.getParamNameValuePairs();
            for (Object key3 : map3.keySet()) {
                map.put("ew.paramNameValuePairs." + key3, map3.get(key3));
            }
            return map;
        } else {
            return super.obj2map(prefix, value);
        }
    }
}
