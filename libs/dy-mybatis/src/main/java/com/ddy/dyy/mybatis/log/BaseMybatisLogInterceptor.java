package com.ddy.dyy.mybatis.log;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

/**
 * mybatis日志美化，不支持oracle
 */
@Slf4j
public class BaseMybatisLogInterceptor implements Interceptor {

    AtomicLong counter = new AtomicLong();

    /**
     * shouldLog
     *
     * @param sql sql
     * @return result
     */
    protected boolean shouldLog(String sql) {
        return true;
    }

    /**
     * intercept
     *
     * @param invocation invocation
     * @return result
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        long startTime = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) target;
        int size = 0;
        try {
            Object o = invocation.proceed();
            if (null != o && isList(o.getClass())) {
                size = ((List) o).size();
            }
            return o;
        } finally {
            try {
                long endTime = System.currentTimeMillis();
                long sqlCost = endTime - startTime;
                BoundSql boundSql = statementHandler.getBoundSql();
                String sql = boundSql.getSql();
                Object parameterObject = boundSql.getParameterObject();
                List<ParameterMapping>
                        parameterMappingList = boundSql.getParameterMappings();
                Map<String, Object> additionalParameters = getAdditionalParameters(boundSql);
                sql = beautifySql(sql); // 美化sql
                if (sql.length() == 0 || parameterObject == null || parameterMappingList.size() == 0) {
                    sql = sql + "";
                } else if (parameterMappingList.size() == 1 && !needFlat(parameterObject.getClass())) {
                    sql = sql.replaceFirst("\\?", getSqlValue(parameterObject));
                } else {
                    Map<String, Object> paramMap = obj2map("", parameterObject);
                    if (paramMap == null) {
                        paramMap = new HashMap<>();
                    }
                    paramMap.putAll(additionalParameters);
                    paramMap = obj2map("", paramMap);
                    for (ParameterMapping parameterMapping : parameterMappingList) {
                        final String property = parameterMapping.getProperty();
                        Object propertyValue = paramMap.get(property);
                        sql = sql.replaceFirst("\\?", getSqlValue(propertyValue));
                    }
                }
                if (shouldLog(sql)) {
                    long seq = counter.incrementAndGet();
                    log(seq, sql, sqlCost, size);
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * @param seq     .
     * @param sql     .
     * @param sqlCost .
     * @param size    .
     */
    protected void log(long seq, String sql, long sqlCost, int size) {
        String txt = "SQL - {}: [{}] 耗时[{}ms] 结果集[{}]";
        log.info(txt, seq, sql, sqlCost, size);
    }


    /**
     * .
     *
     * @param boundSql .
     * @return .
     */
    private static Map<String, Object> getAdditionalParameters(BoundSql boundSql) {
        try {
            Field f = boundSql.getClass().getDeclaredField("additionalParameters");
            f.setAccessible(true);
            Map<String, Object> additionalParameters = (Map<String, Object>) f.get(boundSql);
            if (additionalParameters != null) {
                additionalParameters.remove("_parameter");
                additionalParameters.remove("_databaseId");
            }
            return additionalParameters;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * .
     *
     * @param o .
     * @return .
     */
    private static String getSqlValue(Object o) {
        String v = "";
        if (o == null) {
            v = "null";
        } else if (String.class.isAssignableFrom(o.getClass())) {
            v = "\"" + o + "\"";
        } else if (Enum.class.isAssignableFrom(o.getClass())) { //枚举存int值
            v = o.toString();
        } else if (o instanceof String[]) { //自定义的TypeHandler处理数组
            v = String.join(",", ((String[]) o));
        } else if (o instanceof Date || Temporal.class.isAssignableFrom(o.getClass())) { //日期处理
            v = "\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o) + "\"";
        } else {
            v = o.toString();
        }
        v = chop(v);
        return Matcher.quoteReplacement(v);
    }

    /**
     * plugin
     *
     * @param target target
     * @return result
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * setProperties
     *
     * @param properties properties
     */
    @Override
    public void setProperties(Properties properties) {
        log.info("set properties");
    }


    /**
     * beautifySql
     *
     * @param sql sql
     * @return result
     */
    private String beautifySql(String sql) {
        sql = sql.replaceAll("[\\s\n ]+", " ");
        return sql;
    }


    /**
     * 把对象或map展开，带前缀，可嵌套
     *
     * @param prefix .
     * @param value  .
     * @return .
     */
    protected Map obj2map(String prefix, Object value) {
        Map map = new HashMap();
        if (value == null || value.getClass() == ArrayList.class) {
            return map;
        }

        if (value instanceof Map) {
            Map vmap = (Map) value;
            for (Object field : vmap.keySet()) {
                Object v = vmap.get(field);
                if (v == null) {
                    map.put(prefix + field, "null");
                } else if (needFlat(v.getClass())) {
                    Map map2 = obj2map(prefix + field + ".", v);
                    map.putAll(map2);
                } else {
                    map.put(prefix + field, v);
                }
            }
        } else {

            Field[] fields = value.getClass().getDeclaredFields();
            for (Field field : fields) {

                try {
                    if (field.getName().equals("serialVersionUID")) {
                        continue;
                    }
                    field.setAccessible(true);

                    Object v = field.get(value);
                    if (v == null) {
                        map.put(prefix + field.getName(), "null");
                    } else if (needFlat(v.getClass())) {
                        Map map2 = obj2map(prefix + field.getName() + ".", v);
                        map.putAll(map2);
                    } else {
                        map.put(prefix + field.getName(), v);
                    }
                } catch (IllegalAccessException e) {
                    log.error("error", e);
                }
            }
        }


        return map;
    }

    /**
     * .
     *
     * @param o .
     * @return .
     */
    private static String chop(Object o) {
        if (o == null) {
            return "";
        }
        String str = o.toString();
        if (str.length() > MAX_LEN) {
            return str.substring(0, MAX_LEN) + "(len=" + str.length() + ")\"";
        } else {
            return str;
        }
    }

    private static final int MAX_LEN = 32;

    /**
     * 是否基本数据类型或者基本数据类型的包装类
     *
     * @param parameterObjectClass parameterObjectClass
     * @return result
     */
    private boolean isPrimitiveOrPrimitiveWrapper(Class<?> parameterObjectClass) {
        return parameterObjectClass.isPrimitive()
                || (parameterObjectClass.isAssignableFrom(Byte.class)
                || parameterObjectClass.isAssignableFrom(Short.class)
                || parameterObjectClass.isAssignableFrom(Integer.class)
                || parameterObjectClass.isAssignableFrom(Long.class)
                || parameterObjectClass.isAssignableFrom(Double.class)
                || parameterObjectClass.isAssignableFrom(Float.class)
                || parameterObjectClass.isAssignableFrom(Character.class)
                || parameterObjectClass.isAssignableFrom(Boolean.class)
        );
    }

    /**
     * 是否基本数据类型或者基本数据类型的包装类
     *
     * @param parameterObjectClass parameterObjectClass
     * @return result
     */
    protected boolean needFlat(Class<?> parameterObjectClass) {
        return !(isPrimitiveOrPrimitiveWrapper(parameterObjectClass)
                || parameterObjectClass.isAssignableFrom(String.class)
                || parameterObjectClass.isAssignableFrom(Date.class)
                || parameterObjectClass.isAssignableFrom(BigDecimal.class)
                || parameterObjectClass.isAssignableFrom(BigInteger.class)
                || parameterObjectClass.isAssignableFrom(CharSequence.class)
                || parameterObjectClass.isEnum()
                || parameterObjectClass.isAssignableFrom(LocalDate.class)
                || parameterObjectClass.isAssignableFrom(LocalTime.class)
                || parameterObjectClass.isAssignableFrom(LocalDateTime.class)
        );
    }

    /**
     * 是否DefaultSqlSession的内部类StrictMap
     *
     * @param parameterObjectClass parameterObjectClass
     * @return result
     */
    private boolean isStrictMap(Class<?> parameterObjectClass) {
        return DefaultSqlSession.StrictMap.class.isAssignableFrom(parameterObjectClass);
    }

    /**
     * 是否List的实现类
     *
     * @param clazz clazz
     * @return result
     */
    private boolean isList(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 是否Map的实现类
     *
     * @param parameterObjectClass parameterObjectClass
     * @return result
     */
    private boolean isMap(Class<?> parameterObjectClass) {
        return Map.class.isAssignableFrom(parameterObjectClass);
    }

}
