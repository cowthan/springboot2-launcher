package com.ddy.dyy.mybatis.sqlrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultSet2 {

    private List<Map<String, Object>> rows = null;

    public ResultSet2(List<Map<String, Object>> rows) {
        this.rows = rows;
        if (this.rows == null) {
            this.rows = new ArrayList<>();
        }
    }

    public ResultSet2(Map<String, Object> row) {
        this.rows = new ArrayList<>();
        if (rows != null) {
            this.rows.add(row);
        }
    }

    public Object get(int index, String key) {
        Map<String, Object> row = index >= 0 && index < rows.size() ? rows.get(index) : null;
        if (row != null) {
            return row.get(key.toUpperCase());
        }
        return null;
    }

    public List<Map<String, Object>> rows() {
        return rows;
    }

    public Map<String, Object> row() {
        return rows.size() > 0 ? rows.get(0) : null;
    }
}
