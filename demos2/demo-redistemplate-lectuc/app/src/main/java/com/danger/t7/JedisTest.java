package com.danger.t7;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 <dependency>
     <groupId>redis.clients</groupId>
     <artifactId>jedis</artifactId>
     <version>3.6.3</version>
 </dependency>
 */
public class JedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);  //指定Redis服务Host和port
//        jedis.auth("12345"); //如果Redis服务连接需要密码，制定密码

        jedis.set("key", "1111");
        String value = jedis.get("key"); //访问Redis服务

        System.out.println(value);

        jedis.zadd("aa", 1, "x");
        jedis.zadd("aa", 0.1, "y");
        jedis.zadd("aa", 0.1, "z");
        jedis.zadd("aa", 0.1, "x");

        Tuple t = jedis.zpopmin("aa");
        System.out.println(t.getElement());

        t = jedis.zpopmin("aa");
        System.out.println(t.getElement());

        t = jedis.zpopmin("aa");
        System.out.println(t.getElement());

        t = jedis.zpopmin("aa");
        System.out.println(t.getElement());

        jedis.close(); //使用完关闭连接
    }

}
