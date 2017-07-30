package com.pay.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2017/5/12.
 * Google-Guava
 */
@Configuration()
public class GuavaCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCache.class);
    /**
     * 失效时间
     */
    private int expireTime = 1;
    /**
     * 最大数量
     */
    private int maximumSize = 200;

    @Bean
    public Cache getCache() {
        Cache cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireTime, TimeUnit.MINUTES)
                .removalListener(new RemovalListener() {
                    @Override
                    public void onRemoval(RemovalNotification notification) {
                        LOGGER.info("清除缓存key:{},value:{}", notification.getKey(), notification.getValue());
                    }
                })
                .build();
        return cache;
    }

}
