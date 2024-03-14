package org.danger.utils.db;

import java.util.List;

import org.danger.utils.AssocArray;

/**
 * 类功能描述
 */
public class DbTest {

    public static void main(String[] args) {
        DBConfig dbConfig = new DBConfig();
        dbConfig.setHost("127.0.0.1");
        dbConfig.setPort(8889);
        dbConfig.setUsername("root");
        dbConfig.setPassword("root");
        dbConfig.setDbname("apiadmin");
        MyDb db = new MyDb(dbConfig);

        String sql = "select * from sgw_core_message limit 0, 10";
        List<AssocArray> rows = db.findAll(sql, null);
        System.out.println(rows);
    }
}
