package org.danger.db.raw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 数据库复制
 */
public class DbCopy {

    public static void exportDbToSql(MyDb db, String table, int pageSize, String idField,
                                     String dstTable, Map<String, String> columnMap) throws IOException {
        File file = new File("db_copy.sql");
        FileWriter writer = new FileWriter(file);
        int page = 0;
        boolean hasNext = true;
        while (hasNext) {
            page++;
            long startTime = System.currentTimeMillis();
            int offset = (page - 1) * pageSize;
            String sql = String.format("select * from %s where access_id_int >= 600001 and access_id_int <= 610001  and network_id = 1102 order by %s asc limit %d, %d", table, idField, offset, pageSize);
            List<AssocArray> rows = db.findAll(sql, null);
            hasNext = rows.size() == pageSize;
            long cost = System.currentTimeMillis() - startTime;
            System.out.println(table + " -- 查询" + sql + "\n, page = " + page + ", count = " + rows.size()
                    + ",耗时：" + cost + "ms");

            for (AssocArray row : rows) {

                AssocArray newRow = AssocArray.array();
                // src字段对应到dst字段
                if (columnMap != null) {
                    for (String column : row.keySet()) {
                        if (columnMap.containsKey(column)) {
                            newRow.add(columnMap.get(column), row.get(column));
                        }
                    }
                } else {
                    newRow.putAll(row);
                }

                String insertSql = MyDbUtils.getRawInsertSql(dstTable, newRow, "`");
                System.out.println(insertSql);
                writer.write(insertSql + ";\n");
            }
        }
        writer.close();
    }
}
