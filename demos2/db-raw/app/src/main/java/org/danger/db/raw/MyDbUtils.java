package org.danger.db.raw;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyDbUtils {

    public static boolean isValidDbType(Class<?> clazz){
        if(clazz == Short.class) return true;
        if(clazz == Integer.class) return true;
        if(clazz == Long.class) return true;
        if(clazz == String.class) return true;
        if(clazz == Date.class) return true;

        return false;
    }
    public static NamedJdbcTemplateParam getInsertSql(String table, AssocArray row){
        return getInsertSql(table, row, "");
    }

    public static NamedJdbcTemplateParam getInsertSql(String table, AssocArray row, String nameSuffix){
        StringBuilder sb = new StringBuilder();
        AssocArray pm = AssocArray.array();

        sb.append("insert into `");
        sb.append(table);
        sb.append("` (");
//        sb.append("values (values_cotent)");

        StringBuilder sbFields = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();

        int loop = 0;
        int totalCount = row.size();
        for (Map.Entry<String, Object> entry: row.entrySet()){
            loop++;
            sbFields.append("`" + entry.getKey() + "`");

            sbValues.append(":");
            sbValues.append(entry.getKey() + nameSuffix);

            if(loop != totalCount){
                sbFields.append(", ");
                sbValues.append(", ");
            }

            pm.add(entry.getKey() + nameSuffix, entry.getValue());
        }

        sb.append(sbFields.toString());
        sb.append(") ");
        sb.append("values (");
        sb.append(sbValues.toString());
        sb.append(")");

        return new NamedJdbcTemplateParam(sb.toString(), pm);
    }


    public static String getRawInsertSql(String table, AssocArray row, String columnQuote){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("insert into %s%s%s (", columnQuote, table, columnQuote));
        StringBuilder sbFields = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();

        int loop = 0;
        int totalCount = row.size();
        for (Map.Entry<String, Object> columnAndValue: row.entrySet()){

            loop++;
            sbFields.append(columnQuote + columnAndValue.getKey() + columnQuote);
            Object value = columnAndValue.getValue();
            if(value == null){
                sbValues.append("NULL");
            }else if((value instanceof String)){
                sbValues.append(String.format("'%s'", value));
            }else if((value instanceof Date)){
                sbValues.append(String.format("'%s'", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)value)));
            }else{
                sbValues.append(value);
            }

            if(loop != totalCount){
                sbFields.append(", ");
                sbValues.append(", ");
            }

        }

        sb.append(sbFields.toString());
        sb.append(") ");
        sb.append("values (");
        sb.append(sbValues.toString());
        sb.append(")");

        return sb.toString();
    }

    public static String getInsertSqlForMybatis(String table, AssocArray row, String prefix){
        StringBuilder sb = new StringBuilder();

        sb.append("insert into `");
        sb.append(table);
        sb.append("` (");
//        sb.append("values (values_cotent)");

        StringBuilder sbFields = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();

        int loop = 0;
        int totalCount = row.size();
        for (Map.Entry<String, Object> entry: row.entrySet()){
            loop++;
            sbFields.append("`" + NameUtils.humpToLine2(entry.getKey()) + "`");

            sbValues.append("#{");
            String k = prefix + entry.getKey();
            sbValues.append(k);
            sbValues.append("}");

            if(loop != totalCount){
                sbFields.append(", ");
                sbValues.append(", ");
            }

        }

        sb.append(sbFields.toString());
        sb.append(") ");
        sb.append("values (");
        sb.append(sbValues.toString());
        sb.append(")");

        return sb.toString();
    }


    public static NamedJdbcTemplateParam getUpdateSql(String table, AssocArray row){
        return getUpdateSql(table, row, "");
    }

    public static NamedJdbcTemplateParam getUpdateSql(String table, AssocArray row, String nameSuffix){
        StringBuilder sb = new StringBuilder();
        AssocArray pm = AssocArray.array();

        sb.append("update ");
        sb.append(table);
        sb.append(" set ");

        int loop = 0;
        int totalCount = row.size();

        for (Map.Entry<String, Object> entry: row.entrySet()){
            loop++;

            sb.append("`" + entry.getKey() + "` = :" + entry.getKey() + nameSuffix);
            if(loop != totalCount){
                sb.append(", ");
            }
            pm.add(entry.getKey(), entry.getValue());

            //去除id的话，如果id在最后一个，会出现set系列以逗号结尾的情况，不用纠结了，直接把id也update一下行了
        }

        sb.append(" where id = :id");
        pm.add("id", row.getObject("id", null));

        return new NamedJdbcTemplateParam(sb.toString(), pm);
    }


    public static String getUpdateSqlForMybatis(String table, AssocArray row, String prefix){
        if(prefix == null) prefix = "";
        StringBuilder sb = new StringBuilder();
        AssocArray pm = AssocArray.array();

        sb.append("update ");
        sb.append(table);
        sb.append(" set ");

        int loop = 0;
        int totalCount = row.size();

        for (Map.Entry<String, Object> entry: row.entrySet()){
            loop++;
//            if("id".equals(entry.getKey())) {
//                continue;
//            }else{
//                sb.append("`" + entry.getKey() + "` = :" + entry.getKey());
//                if(loop != totalCount){
//                    sb.append(", ");
//                }
//                pm.add(entry.getKey(), entry.getValue());
//            }

            sb.append("`" + NameUtils.humpToLine2(entry.getKey()) + "` = #{" + prefix + entry.getKey() + "}");
            if(loop != totalCount){
                sb.append(", ");
            }
            pm.add(entry.getKey(), entry.getValue());

            //去除id的话，如果id在最后一个，会出现set系列以逗号结尾的情况，不用纠结了，直接把id也update一下行了
        }

        sb.append(" where id = #{" + prefix + "id}");
        pm.add("id", row.getObject("id", null));

        return sb.toString();
    }

    public static AssocArray parse(Class<?> clazz, Object o, boolean ignoreNull){
        if(o == null) return null;
        AssocArray row = new AssocArray();

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            if(!isValidDbType(f.getType())) continue;
            if(f.getAnnotation(DbIgnore.class) != null) continue;
            String key = f.getName();
            try {
                Object value = f.get(o);
                if(ignoreNull && value == null) continue;
                row.add(key, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }

        return row;
    }



}
