package com.ddy.dyy.mybatis;

import java.util.List;

import com.ddy.dyy.web.models.biz.AssocArray;


public class DbSupport {

    public static List<AssocArray> findAll(String sql, AssocArray map) {
        return DyMybatisAutoConfiguration.getContext().getBean(SchemeService.class).select(sql, map);
    }

    public static AssocArray findOne(String sql, AssocArray map) {
        List<AssocArray> list = findAll(sql, map);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public static int update(String sql, AssocArray map) {
        return DyMybatisAutoConfiguration.getContext().getBean(SchemeService.class).update(sql, map);
    }

    public static Object findValue(String sql, AssocArray map) {
        List<AssocArray> list = findAll(sql, map);
        AssocArray row = (list == null || list.isEmpty()) ? null : list.get(0);
        if (row == null) return null;
        String key = null;
        for (String k : row.keySet()) {
            key = k;
            break;
        }
        return row.get(key);
    }

    public static int findInt(String sql, AssocArray map) {
        Object value = findValue(sql, map);
        try {
            return Integer.parseInt(value + "");
        } catch (Exception e) {
            return 0;
        }
    }

    public static long findLong(String sql, AssocArray map) {
        Object value = findValue(sql, map);
        try {
            return Integer.parseInt(value + "");
        } catch (Exception e) {
            return 0;
        }
    }

}
