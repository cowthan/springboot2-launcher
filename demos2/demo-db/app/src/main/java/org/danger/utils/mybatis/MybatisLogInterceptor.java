package org.danger.utils.mybatis;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mybatis日志美化，不支持oracle
 */
@Slf4j
@Component
//@Profile({"dev", "test"})
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class MybatisLogInterceptor implements Interceptor {

    AtomicLong counter = new AtomicLong();

    @Value("${mybatis-plus.logEnable}")
    private boolean logEnable;

    /**
     * shouldLog
     *
     * @param sql sql
     * @return result
     */
    private boolean shouldLog(String sql) {
        if (logEnable && sql.contains("sgw")) {
            return true;
        }
        return false;
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
            if (isList(o.getClass())) {
                size = ((List) o).size();
            }
            return o;
        } finally {
            long endTime = System.currentTimeMillis();
            long sqlCost = endTime - startTime;

            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            Object parameterObject = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();

            // 格式化Sql语句，去除换行符，替换参数
            sql = formatSql(sql, parameterObject, parameterMappingList);

            if (shouldLog(sql)) {
                long seq = counter.incrementAndGet();
                String txt = "SQL - {}: [" + sql + "]执行耗时[" + sqlCost + "ms]结果集[" + size + "]";
                log.info(txt, seq);
//                String params = SqlParamUtils.getParamAsJsonString(parameterObject, parameterMappingList);
//                log.info("SQL param - {}: {}", seq, params);

            }
        }
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
     * 增加代码可以处理通用mapper的Example参数美化sql
     *
     * @param sql                  美化前sql
     * @param parameterObject      参数
     * @param parameterMappingList 参数map
     * @return 美化后sql
     */
    private String formatSql(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
        // 输入sql字符串空判断
        if (sql == null) {
            sql = "";
        }

        // 美化sql
        sql = beautifySql(sql);

        // 不传参数的场景，直接把Sql美化一下返回出去
        if (sql.length() == 0 || parameterObject == null || parameterMappingList.size() == 0) {
            return sql;
        }

        // 定义一个没有替换过占位符的sql，用于出异常时返回
        String sqlWithoutReplacePlaceholder = sql;


        try {
            Class<?> parameterObjectClass = parameterObject.getClass();

            // 如果参数是StrictMap且Value类型为Collection，获取key="list"的属性，这里主要是为了处理<foreach>循环时传入List这种参数的占位符替换
            // 例如select * from xxx where id in <foreach collection="list">...</foreach>
            if (isStrictMap(parameterObjectClass)) {
                DefaultSqlSession.StrictMap<Collection<?>> strictMap =
                        (DefaultSqlSession.StrictMap<Collection<?>>) parameterObject;

                if (isList(strictMap.get("list").getClass())) {
                    sql = handleListParameter(sql, strictMap.get("list"), parameterMappingList);
                }
            } else if (isMap(parameterObjectClass)) {
                // 如果参数是Map则直接强转，通过map.get(key)方法获取真正的属性值
                // 这里主要是为了处理<insert>、<delete>、<update>、<select>时传入parameterType为map的场景
                Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
                sql = handleMapParameter(sql, paramMap, parameterMappingList);
            } else {
                // 通用场景，比如传的是一个自定义的对象或者八种基本数据类型之一或者String
                sql = handleCommonParameter(sql, parameterMappingList, parameterObjectClass, parameterObject);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 占位符替换过程中出现异常，则返回没有替换过占位符但是格式美化过的sql，这样至少保证sql语句比BoundSql中的sql更好看
            return sqlWithoutReplacePlaceholder;
        }

        return sql;
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
     * 处理参数为List的场景
     *
     * @param sql                  sql
     * @param col                  col
     * @param parameterMappingList parameterMappingList
     * @return result
     */
    private String handleListParameter(String sql, Collection<?> col, List<ParameterMapping> parameterMappingList) {
        final Object[] objects = col.toArray();
        final int groupSize = parameterMappingList.size() / objects.length;
        for (int i = 0; i < objects.length; i++) {
            sql = this.formatSql(sql, objects[i], parameterMappingList.subList(i * groupSize, (i + 1) * groupSize - 1));
        }
        return sql;
    }

    /**
     * 处理参数为Map的场景
     *
     * @param sql                  sql
     * @param paramMap             paramMap
     * @param parameterMappingList parameterMappingList
     * @return result
     *
     */
    private String handleMapParameter(String sql, Map<?, ?> paramMap,
                                      List<ParameterMapping> parameterMappingList) {
        List<ParameterMapping> current = new ArrayList<>();
        final HashMap<Object, Object> currentMap = new HashMap<>();
        currentMap.putAll(paramMap);
        for (ParameterMapping parameterMapping : parameterMappingList) {
            final String property = parameterMapping.getProperty();
            final String fieldName = property.startsWith("__frch_record_")
                    ? property.substring(property.indexOf(".") + 1)
                    : property;
            Object propertyValue = currentMap.get(fieldName);
            current.add(parameterMapping);
            sql = this.formatSql(sql, propertyValue, current);
            current.clear();
        }

        return sql;
    }

    /**
     * 处理通用的场景
     *
     * @param sql                  sql
     * @param parameterMappingList parameterMappingList
     * @param parameterObjectClass parameterObjectClass
     * @param parameterObject      parameterObject
     * @return result
     *
     * @throws IntrospectionException .
     * @throws InvocationTargetException .
     * @throws IllegalAccessException .
     */
    private String handleCommonParameter(String sql, List<ParameterMapping> parameterMappingList,
                                         Class<?> parameterObjectClass,
                                         Object parameterObject)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        String propertyValue = "null";
        // 基本数据类型或者基本数据类型的包装类，直接toString即可获取其真正的参数值，其余直接取paramterMapping中的property属性即可
        if (isPrimitiveOrPrimitiveWrapper(parameterObjectClass)) {
            propertyValue = parameterObject.toString();
        } else if (String.class.isAssignableFrom(parameterObjectClass)) {
            propertyValue = "\"" + parameterObject + "\"";
        } else if (Enum.class.isAssignableFrom(parameterObjectClass)) { //枚举存int值
            propertyValue = String.valueOf(((Enum) parameterObject).ordinal());
        } else if (parameterObject instanceof String[]) { //自定义的TypeHandler处理数组
            propertyValue = String.join(",", ((String[]) parameterObject));
        } else if (parameterObject instanceof Date || Temporal.class.isAssignableFrom(parameterObjectClass)) { //日期处理
            propertyValue = "\"" + parameterObject.toString() + "\"";
        } else if (parameterMappingList != null && parameterMappingList.size() > 0) {
            for (ParameterMapping parameterMapping : parameterMappingList) {
                final String property = parameterMapping.getProperty();
                final String fieldName = property.startsWith("__frch_record_")
                        ? property.substring(property.indexOf(".") + 1)
                        : property;
                final PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, parameterObjectClass);
                final Method readMethod = propertyDescriptor.getReadMethod();
                final Object invoke = readMethod.invoke(parameterObject);
                if (Objects.isNull(invoke)) {
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(propertyValue));
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put(fieldName, invoke);
                    sql = this.formatSql(sql, map, parameterMappingList);
                    //sql = this.formatSql(sql, ImmutableMap.of(fieldName, invoke), parameterMappingList);
                }
            }
        }

        if (propertyValue != null && !propertyValue.equals("")) {
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(propertyValue));
        }

        return sql;
    }

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
                || parameterObjectClass.isAssignableFrom(Boolean.class));
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
