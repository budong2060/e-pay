package com.pay.client.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by heyinbo on 2017/7/14.
 * 构建restTemple
 */
@Configuration()
public class RestConfig {

    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;

    private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;

    private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);
    /**
     * 最大连接数
     */
    private int maxTotal = DEFAULT_MAX_TOTAL_CONNECTIONS;
    /**
     * 每个站点最大连接数
     */
    private int maxTotalPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;
    /**
     * 读取超时时间
     */
    private int readTimeOut = DEFAULT_READ_TIMEOUT_MILLISECONDS;

    public ClientHttpRequestFactory buildFactory() {
        HttpClientBuilder builder = HttpClientBuilder.create().useSystemProperties();
        builder.setMaxConnTotal(maxTotal);              //设置最大连接数
        builder.setMaxConnPerRoute(maxTotalPerRoute);   //设置每个站点最大连接数
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(builder.build());
        requestFactory.setReadTimeout(readTimeOut);
        return requestFactory;
    }

    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory factory = buildFactory();
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }
}
