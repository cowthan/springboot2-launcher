package com.danger.t7.controller;

import java.time.Duration;
import java.util.List;

import com.danger.t7.JsonRedisTemplate;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Autowired
//    io.lettuce.core.RedisClient redisClient;
//
//    @Autowired
//    io.lettuce.core.api.sync.RedisCommands<String, String> commands;

    // http://localhost:8080/test/test1
    @RequestMapping("test1")
    public String test1() {

        redisTemplate.opsForValue().set("x", 1);
        System.out.println(redisTemplate.opsForValue().get("x"));
        return "123";
    }


    public static void main(String[] args) {
//        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
//        conf.setHostName("172.16.101.86");
//        conf.setPort(7000);
//        conf.setPassword("t%s*t!e");
//        conf.setDatabase(0);
//        RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(conf);

        JsonRedisTemplate template = createRedisTemplate("172.16.101.86",
                7000,
                "t%s*t!e",
                15,
                8, 8, -1L, 0);

        template.opsForValue().set("aaaa", 1);
        System.out.println(template.opsForValue().get("aaaa"));

        String lua = "local ret={} " +
                "ret[0] = redis.call('HINCRBY', 'a', 'b', 2) " +
                "return ret";
        RedisScript<List> luaScript = new DefaultRedisScript<>(lua.toString(), List.class);
        template.execute(luaScript, null);
    }

    public static JsonRedisTemplate createRedisTemplate(String host, Integer port, String password, Integer database, Integer maxActive, Integer maxIdle, Long maxWait, Integer minIdle) {
        GenericObjectPoolConfig poolConfig = localPoolConfig(maxActive, maxIdle, maxWait, minIdle);
        RedisStandaloneConfiguration redisConfig = localRedisConfig(host, port, password, database);
        LettuceConnectionFactory connectionFactory = localLettuceConnectionFactory(redisConfig, poolConfig);
        JsonRedisTemplate template = new JsonRedisTemplate(connectionFactory);

        return template;
    }

    public static GenericObjectPoolConfig localPoolConfig(Integer maxActive, Integer maxIdle, Long maxWait, Integer minIdle) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWait);
        return config;
    }

    public static RedisStandaloneConfiguration localRedisConfig(String host, Integer port, String password, Integer database) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPassword(RedisPassword.of(password));
        config.setPort(port);
        config.setDatabase(database);
        return config;
    }

    public static RedisTemplate<String, String> localRedisTemplate(LettuceConnectionFactory localLettuceConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(localLettuceConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public static LettuceConnectionFactory localLettuceConnectionFactory(RedisStandaloneConfiguration localRedisConfig,
                                                                         GenericObjectPoolConfig localPoolConfig) {
        LettuceClientConfiguration clientConfig =
                LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(60000))
                        .poolConfig(localPoolConfig).build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(localRedisConfig, clientConfig);
        factory.afterPropertiesSet();
        return factory;
    }

}
