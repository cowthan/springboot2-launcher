package org.danger.utils.db.doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;

import org.danger.utils.AssocArray;
import org.danger.utils.Lang;
import org.danger.utils.db.MyDb;


public class DatabaseDocSupport {

    public static void createTableClass(MyDb db){
        List<String> tables = getTablesNames(db);
        List<DbTableInfo> tableInfos = new ArrayList<>();
        for (String table: tables){
            DbTableInfo dbTableInfo = extractTable(table);
            tableInfos.add(dbTableInfo);

//            System.out.println(JsonUtils.toJson(dbTableInfo));
//            if(1+1==2) continue;

            // 生成SqlTable类
            String tpl = "package com.danger.data.table;\n" +
                    "\n" +
                    "import org.mybatis.dynamic.sql.SqlColumn;\n" +
                    "import org.mybatis.dynamic.sql.SqlTable;\n" +
                    "\n" +
                    "import java.sql.JDBCType;\n" +
                    "import java.math.BigDecimal;\n" +
                    "import java.util.Date;\n" +
                    "\n" +
                    "public class {{table}} extends SqlTable {\n" +
                    "\n" +
                    "    public static final {{table}} t = new {{table}}();\n" +
                    "\n" +
                    " {{columns}}" +
                    "\n" +
                    "    public {{table}}() {\n" +
                    "        super(\"{{table}}\");\n" +
                    "    }\n" +
                    "}";
            tpl = tpl.replace("{{table}}", table);

            // "    public final SqlColumn<Long> id = column(\"id\", JDBCType.INTEGER);\n" +
            // "    public final SqlColumn<String> sid = column(\"sid\", JDBCType.VARCHAR);\n" +
            // "    public final SqlColumn<String> username = column(\"username\", JDBCType.VARCHAR);\n" +

            String columns = "";
            for (DbColumnInfo c: dbTableInfo.columns){
                Map<String, String> typeMap = new HashMap<>();
                typeMap.put("int", "Integer");
                typeMap.put("tinyint", "Integer");
                typeMap.put("smallint", "Integer");
                typeMap.put("bigint", "Long");
                typeMap.put("decimal", "BigDecimal");
                typeMap.put("float", "Double");
                typeMap.put("double", "Double");
                typeMap.put("text", "String");
                typeMap.put("varchar", "String");
                typeMap.put("char", "String");
                typeMap.put("char", "String");
                typeMap.put("timestamp", "Date");
                typeMap.put("datetime", "Date");

                Map<String, String> typeMap2 = new HashMap<>();
                typeMap2.put("int", "INTEGER");
                typeMap2.put("tinyint", "TINYINT");
                typeMap2.put("smallint", "SMALLINT");
                typeMap2.put("bigint", "BIGINT");
                typeMap2.put("decimal", "DECIMAL");
                typeMap2.put("float", "FLOAT");
                typeMap2.put("double", "DOUBLE");
                typeMap2.put("text", "LONGVARCHAR");
                typeMap2.put("varchar", "VARCHAR");
                typeMap2.put("char", "CHAR");
                typeMap2.put("timestamp", "TIMESTAMP");
                typeMap2.put("datetime", "DATE");

//                String type = c.type;
                System.out.println(c.type);
                List<String> typeParts = Lang.splitToList(c.type, "\\(");
                String type2 = typeParts.get(0);
                int typeLen = 0;
                if(typeParts.size() >= 2) typeLen = Lang.toInt(typeParts.get(1).replace(")", "").replace("unsigned", "").trim());

                String javaType = typeMap.get(type2);
                if(type2.equals("int")){
                    if(typeLen >= 10) javaType = "Long";
                }

                String type222 = typeMap2.get(type2);
                String columnJava = "public final SqlColumn<%s> %s = column(\"%s\", JDBCType.%s)";
                columnJava = String.format(columnJava, javaType, c.name, c.name, type222);
                c.columnJava1 = columnJava;
            }


            for (DbColumnInfo c: dbTableInfo.columns){
                columns += c.columnJava1 + ";\n";
            }

            tpl = tpl.replace("{{columns}}", columns);

            String path = "/Users/cowthan/Desktop/ws2021-2/项目启动模板/api-support/code-generate/tables/";
            path += table + ".java";
            Lang.file_put_content(path, tpl);
        }
    }

    public static void createDbDocToDocify() {
        String path = MyStorage.getLocalFile("runtime/db_doc/README.md").getAbsolutePath();
        String content = generateDatabaseMarkdown();
        Lang.file_put_content(path, content);

        String indexTpl = MyStorage.read("assets/md_doc_tpl/index.html");
        indexTpl = indexTpl.replace("${title}", "数据库文档");
        String path2 = MyStorage.getLocalFile("runtime/db_doc/index.html").getAbsolutePath();
        Lang.file_put_content(path2, indexTpl);
    }

    private static String generateDatabaseMarkdown(MyDb db) {
        List<AssocArray> tables = db.findAll("show tables", null);
        List<String> tableNames = tables.stream().map(e -> e.getValueForFirstKey()).collect(Collectors.toList());

        String content = "# 数据库";

        int count = 0;
        for (String table : tableNames) {
            count++;
            List<AssocArray> columns = db.findAll("show full columns from " + table, null);
            String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_name = '" + table + "'";
            List<AssocArray> comment = db.findAll(sql, null);
            String tableComment = comment.get(0).getValueForFirstKey();
            content += "\n## " + count + " " + table + " " + tableComment + "\n\n";

//            content += String.format("\n[生成代码](%s/dev/code/from_sql?tableName=%s)\n", Env.getHost(), table);
            content += String.format("\n[生成代码](%s/dev/code/from_sql?tableName=%s)\n", "", table);

            content += "\n\n>表字段\n\n| 序号 |  字段名  |  数据类型  |  备注  |\n";
            content += "| ------ | ------ | ------ | ------ |\n";

            int count2 = 0;
            for (AssocArray column : columns) {
                count2++;
                content +=
                        "| " + count2
                                + " | " + column.getString("Field")
                                + " | " + column.getString("Type")
                                + " | " + column.getString("Comment")
                                + " | \n";
            }

            sql = "show create table " + table;
            List<AssocArray> rows = db.findAll(sql, null);
            String createSql = rows.get(0).getString("Create Table");
            content += "\n\n>建表语句\n\n```\n" + createSql + "\n```\n\n";
        }


        return content;
    }


    public static List<DbTableInfo> getDatabaseInfos(MyDb db){
        List<String> tables = getTablesNames(db);
        List<DbTableInfo> tableInfos = new ArrayList<>();
        for (String table: tables) {
            DbTableInfo dbTableInfo = extractTable(db, table);
            tableInfos.add(dbTableInfo);
        }
        return tableInfos;
    }

    public static List<String> getTablesNames(MyDb db){
        List<AssocArray> tables = db.findAll("show tables", null);
        List<String> tableNames = tables.stream().map(e -> e.getValueForFirstKey()).collect(Collectors.toList());
        return tableNames;
    }

    public static DbTableInfo extractTable(MyDb db, String table) {
        DbTableInfo t = new DbTableInfo();
        t.tableName = table;

        String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_name = '" + table + "'";
        List<AssocArray> comment = db.findAll(sql, null);
        t.tableComment = Lang.isEmpty(comment) ? "" : comment.get(0).getValueForFirstKey();

        sql = "show create table " + table;
        List<AssocArray> rows = db.findAll(sql, null);
        t.createSql = rows.get(0).getString("Create Table");

        List<AssocArray> columns = db.findAll("show full columns from " + table, null);

        t.columns = new ArrayList<>();
        for (AssocArray column : columns) {
            DbColumnInfo c = new DbColumnInfo();
            c.name = column.getString("Field");
            c.type = column.getString("Type");
            c.comment = column.getString("Comment");
            c.constrain = "";
            t.columns.add(c);
        }

        return t;
    }

}
