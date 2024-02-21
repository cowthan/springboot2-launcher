package com.danger.t7.demo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

/**
 * CaffeinCache
 */
public class CaffeineCache {

    private static final class Holder{
        private static final CaffeineCache INSTANCE = new CaffeineCache();
    }
    private CaffeineCache(){

    }
    public static CaffeineCache sharedInstance(){
        return Holder.INSTANCE;
    }

    private ConcurrentHashMap<String, Cache<String, Object>> caches = new ConcurrentHashMap<>();

    /**
     * When cached data expired in ehcache, this listener will be invoked.
     *
     * @author Winter Lau(javayou@gmail.com)
     */
    public interface CacheExpiredListener {

        /**
         * 缓存因为超时失效后触发的通知
         * @param region 缓存 region
         * @param key 缓存 key
         */
        void notifyElementExpired(String region, String key) ;

    }

    public Cache<String, Object> get(String region){
        return caches.computeIfAbsent(region, v -> {
            return newCaffeineCache(region, 10_000_00L, -1L, new CacheExpiredListener() {
                @Override
                public void notifyElementExpired(String region, String key) {
                    System.out.println("缓存过期：" + region + "==>" + key);
                }
            });
        });
    }

    /**
     * 返回对 Caffeine cache 的 封装
     * @param region region name
     * @param size   max cache object size in memory
     * @param expire cache object expire time in second
     *               if this parameter set to 0s or negative numbers
     *               means never expire
     * @param listener  j2cache cache listener
     * @return CaffeineCache
     */
    private Cache<String, Object> newCaffeineCache(String region, long size, long expire, CacheExpiredListener listener) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        caffeine = caffeine.maximumSize(size)
                .removalListener((k,v, cause) -> {
                    /*
                     * 程序删除的缓存不做通知处理，因为上层已经做了处理
                     * 当缓存数据不是因为手工删除和超出容量限制而被删除的情况，就需要通知上层侦听器
                     */
                    if(cause != RemovalCause.EXPLICIT && cause != RemovalCause.REPLACED && cause != RemovalCause.SIZE)
                        listener.notifyElementExpired(region, (String)k);
                });
        if (expire > 0) {
            caffeine = caffeine.expireAfterWrite(expire, TimeUnit.SECONDS);
        }
        Cache<String, Object> loadingCache = caffeine.build();
        return loadingCache;
    }
    /*
    @Override
    public Object get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public Map<String, Object> get(Collection<String> keys) {
        return cache.getAllPresent(keys);
    }

    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void put(Map<String, Object> elements) {
        cache.putAll(elements);
    }

    @Override
    public Collection<String> keys() {
        return cache.asMap().keySet();
    }

    @Override
    public void evict(String...keys) {
        cache.invalidateAll(Arrays.asList(keys));
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }
     */
}
