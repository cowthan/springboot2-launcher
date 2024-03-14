package com.danger.t7;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * serialize value as json
 */
public class JsonRedisTemplate extends RedisTemplate<String, Object> {

    /**
     * constructor
     * @param connectionFactory .
     */
    public JsonRedisTemplate(RedisConnectionFactory connectionFactory) {
        setKeySerializer(RedisSerializer.string());
        setHashKeySerializer(RedisSerializer.string());
        setValueSerializer(defaultRedisJsonSerializer());
        setHashValueSerializer(defaultRedisJsonSerializer());

        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    /**
     * @return default json serializer
     */
    private static RedisSerializer<Object> defaultRedisJsonSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

}
