package com.ddy.dyy.web.lang;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
    private static ObjectMapper objectMapper = null;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    }

    private JsonUtils() {

    }

    private static ObjectMapper getMapper() {
        return objectMapper;
    }

    public static String toJson(Object o) {
        try {
            return getMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * entity to json str
     *
     * @param o              entity
     * @param date2Timestamp Date字段是否以毫秒返回
     * @return json
     */
    public static String toJson(Object o, boolean date2Timestamp) {
        if (o == null) {
            return "";
        }
        try {
            if (date2Timestamp) {
                return getMapper()
                        .writer(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(o);
            } else {
                return getMapper().writeValueAsString(o);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonPretty(Object o) {
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(String json, Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T toObj(String json, TypeReference<T> type) {
        try {
            T t = getMapper().readValue(json, type);
            return t;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toList(String json, Class<T> elementClazz) {
        try {
            JavaType javaType = getMapper().getTypeFactory().constructParametricType(List.class, elementClazz);
            List<T> list = getMapper().readValue(json, javaType);
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Set<T> toSet(String json, Class<T> elementClazz) {
        try {
            JavaType javaType = getMapper().getTypeFactory().constructParametricType(Set.class, elementClazz);
            Set<T> list = getMapper().readValue(json, javaType);
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
