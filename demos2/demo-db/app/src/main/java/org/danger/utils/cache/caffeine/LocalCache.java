package org.danger.utils.cache.caffeine;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.danger.utils.cache.DataLoader;

/**
 * https://www.jianshu.com/p/3434991ad075
 */
public class LocalCache {

    private static Cache<String, Object> cacheLoader = null;

    private static Cache<String, Object> cache() {
        if(cacheLoader == null){
            Cache<String, Object> cacheLoader = Caffeine.newBuilder()
                    .expireAfterWrite(3, TimeUnit.DAYS.MINUTES)
                    .maximumSize(100)
                    .build();
            LocalCache.cacheLoader = cacheLoader;
        }
        return LocalCache.cacheLoader;
    }


    public static void set(String key, DataLoader dataLoader){
        try {
            cache().put(key, dataLoader.load(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void set(String key, Object data){
        try {
            cache().put(key, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object get(String key){
        try {
            return cache().getIfPresent(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getOrSet(String key, DataLoader dataLoader){
        Object data = cache().getIfPresent(key);
        if(data == null){
            data = dataLoader.load(key);
            cache().put(key, data);
        }
        return data;
    }

    public static void main(String[] args) {
        LocalCache.set("a", new DataLoader() {
            @Override
            public Object load(String key) {
                return 123;
            }
        });

        System.out.println(LocalCache.get("a"));
    }
}
