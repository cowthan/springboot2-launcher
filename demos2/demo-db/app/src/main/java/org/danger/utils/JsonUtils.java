package org.danger.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * json工具类
 *
 * 备注：常用来打调试日志用
 */
public final class JsonUtils {

    private JsonUtils() {

    }

    /**
     * 返回ObjectMapper实例，如果不是在spring环境下就直接new
     *
     * @return ObjectMapper实例
     */
    private static ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        return objectMapper;
    }

    /**
     * entity to json str
     *
     * @param o entity
     * @return json
     */
    public static String toJson(Object o) {
        try {
            return getMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * entity to json str with format
     *
     * @param o entity
     * @return json
     */
    public static String toJsonPretty(Object o) {
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json to entity
     *
     * @param json  json str
     * @param clazz entity class
     * @param <T>   entity type
     * @return entity
     */
    public static <T> T toObj(String json, Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map toObj(String json) {
        if(Lang.isEmpty(json)) return new HashMap();
        try {
            return getMapper().readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map> toList(String json) {
        ObjectMapper objectMapper = getMapper();
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Map.class);
            List<Map> list = objectMapper.readValue(json, javaType);
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json to entity
     *
     * @param json json str
     * @param type 泛型
     * @param <T>  entity type
     * @return entity
     */
    public static <T> T toObj(String json, TypeReference<T> type) {
        try {
            T t = getMapper().readValue(json, type);
            return t;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json str to entity list
     *
     * @param json         json str
     * @param elementClazz element class
     * @param <T>          element type
     * @return list
     */
    public static <T> List<T> toList(String json, Class<T> elementClazz) {
        ObjectMapper objectMapper = getMapper();
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, elementClazz);
            List<T> list = objectMapper.readValue(json, javaType);
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json str to entity set
     *
     * @param json         json str
     * @param elementClazz element class
     * @param <T>          element type
     * @return list
     */
    public static <T> Set<T> toSet(String json, Class<T> elementClazz) {
        ObjectMapper objectMapper = getMapper();
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Set.class, elementClazz);
            Set<T> list = objectMapper.readValue(json, javaType);
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

