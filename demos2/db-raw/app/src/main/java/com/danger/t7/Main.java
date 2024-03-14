package com.danger.t7;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.danger.db.raw.DBConfig;
import org.danger.db.raw.DbCopy;
import org.danger.db.raw.MyDb;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) throws IOException {
        DBConfig dbConfig = new DBConfig();
        dbConfig.setHost("172.16.101.93");
        dbConfig.setPort(3306);
        dbConfig.setUsername("root");
        dbConfig.setPassword("Pwd_2018");
        dbConfig.setDbname("satellite-meta-service");
        MyDb db = new MyDb(dbConfig);

        Map<String, String> columnMap_oldFirst = null;
        columnMap_oldFirst = new HashMap<>();
//        columnMap_oldFirst.put("id", "id");
        columnMap_oldFirst.put("network_id", "network_id");
        columnMap_oldFirst.put("access_id_int", "access_id");
        columnMap_oldFirst.put("entity_id", "entity_id");
        columnMap_oldFirst.put("entity_type", "entity_type");
        columnMap_oldFirst.put("entity_model", "entity_model");
        columnMap_oldFirst.put("entity_code", "entity_code");
        DbCopy.exportDbToSql(db, "access_id_bindings", 1000, "id",
                "meta.cn_meta_access_id_bindings", columnMap_oldFirst);
    }
}
