package com.pay.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2017/7/4.
 * spring集成自定义Guava cache
 */
public class CustomRedisCache implements Cache {

    /**
     * 缓存默认名字
     */
    private final String DEFAULT_NAME = CacheEnum.GUAVA.getCode();

    private String name = DEFAULT_NAME;
    /**
     * 是否允许空值
     */
    private boolean allowNullValues;

    private com.google.common.cache.Cache cache;

    public CustomRedisCache(String name) {
        this(name, CacheBuilder.newBuilder()
                .maximumSize(200)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(), true);
    }

    public CustomRedisCache(String name, com.google.common.cache.Cache cache) {
        this(name, cache, true);
    }

    public CustomRedisCache(String name, com.google.common.cache.Cache cache, boolean allowNullValues) {
        this.cache = cache;
        this.name = name;
        this.allowNullValues = allowNullValues;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = cache.getIfPresent(key);
        return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = cache.getIfPresent(key);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        try {
            return (T) cache.get(key, valueLoader);
        } catch (Throwable ex) {
            throw new ValueRetrievalException(key, valueLoader, ex);
        }
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        cache.put(key, value);
        return new SimpleValueWrapper(fromStoreValue(value));
    }

    @Override
    public void evict(Object key) {
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }

    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues && storeValue == null) {
            return null;
        }
        return storeValue;
    }
}
