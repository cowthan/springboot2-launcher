package org.danger.utils.db;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.danger.utils.AssocArray;
import org.springframework.jdbc.core.RowMapper;

class GenericMapper implements RowMapper<AssocArray> {
    @Override
    public AssocArray mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        AssocArray row = new AssocArray();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            Object value = rs.getObject(columnName);
            row.put(columnName, value);
        }
        return row;
    }
}
