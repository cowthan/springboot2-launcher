package com.danger.t7.demo;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import lombok.Getter;
import lombok.Setter;

/**
 * Test
 */
public class Test {

    @Getter @Setter
    public static class User{
        private String name;

        public User(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Cache<String, Object> cache = CaffeineCache.sharedInstance().get("default");
        cache.put("王", loadUser("王"));
        while (true){
            User user = (User) cache.getIfPresent("王");

            System.out.println(user == null ? "没有缓存" : user.getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static User loadUser(String key){
        System.out.println("加载缓存");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User(key);
    }


}
