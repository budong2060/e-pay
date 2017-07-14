package com.pay.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by admin on 2017/7/4.
 */
@Configuration
@EnableCaching
@ComponentScan("com.pay.cache")
public class GuavaCacheConfig {

    @Autowired
    private com.google.common.cache.Cache cache;

    @Bean
    @Primary
    public CacheManager getCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        Collection<CustomGuavaCache> caches = new ArrayList<>();
        caches.add(new CustomGuavaCache(CacheEnum.GUAVA.getCode(), cache));
        cacheManager.setCaches(caches);
        return cacheManager;
    }

}
