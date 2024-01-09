package com.ddy.dyy.web.models.biz;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class AssocArray extends LinkedHashMap<String, Object> {

    public static AssocArray array(){
        return new AssocArray();
    }

    public String getValueForFirstKey(){
        for (String key: keySet()){
            return getString(key);
        }
        return "";
    }

    public AssocArray add(String k, Object v){
        put(k, v);
        return this;
    }

    public Object getData(String k, Object defaultValue){
        if(containsKey(k)){
            return get(k);
        }
        return defaultValue;
    }

    public <T> T getObject(String k, T defaultValue){
        if(containsKey(k)){
            return (T)get(k);
        }
        return defaultValue;
    }

    public int getInt(String key, int defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        if(v instanceof Integer) return (int) v;
        if(v instanceof String) {
            return Integer.parseInt((String)v);
        }else if(v instanceof BigInteger){
            return ((BigInteger) v).intValue();
        }if(v instanceof Long) {
            return Integer.parseInt(v + "");
        }
        return (Integer)v; //等着抛异常就行了，不要在这里catch
    }
    public int getInt(String key){
        return getInt(key, 0);
    }

    public long getLong(String key){
        return getLong(key, 0);
    }

    public long getLong(String key, int defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        if(v instanceof Integer) return (int) v;
        if(v instanceof String) {
            return Long.parseLong((String)v);
        }else if(v instanceof BigInteger){
            return ((BigInteger) v).longValue();
        }if(v instanceof Long) {
            return (Long)v;
        }
        return (Long)v; //等着抛异常就行了，不要在这里catch
    }

    public Date getDate(String key){
        return getDate(key, null);
    }

    public Date getDate(String key, Date defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        if(v instanceof Timestamp){
            Timestamp t = (Timestamp) v;
            return new Date(t.getTime());
        }else if(v instanceof Date){
            Date t = (Date) v;
            return t;
        }
        return (Date)v; //等着抛异常就行了，不要在这里catch
    }
    public double getDouble(String key){
        return getDouble(key, 0);
    }
    public double getDouble(String key, double defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        if(v instanceof BigDecimal){
            return ((BigDecimal)v).doubleValue();
        }
        return (Double) v; //等着抛异常就行了，不要在这里catch
    }
    public String getString(String key, String defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        return v.toString();
    }
    public String getString(String key){
        return getString(key, "");
    }
//    public Object getObject(String key, Object defaultValue){
//        Object v = get(key);
//        if(v == null) return defaultValue;
//        return v;
//    }

    public void incrementInt(String key, int num){
        int current = 0;
        if(containsKey(key)){
            current = (int) get(key);
        }
        put(key, current + num);
    }
    public void incrementLong(String key, long num){
        long current = 0;
        if(containsKey(key) && get(key) != null){
            current = (long) get(key);
        }
        put(key, current + num);
    }

    public void each(Callback callback){
        if(!isEmpty()){
            forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(String s, Object o) {
                    callback.onEntry(s, o);
                }
            });
        }
    }

    public static interface Callback{
        void onEntry(String key, Object value);
    }
}
