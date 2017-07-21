package com.pay.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.TimeUnit;

/**
 * Created by Halbert on 2017/7/1.
 */
@ComponentScan(value = "com.pay.*")
@EnableAutoConfiguration
@ImportResource("classpath:spring-base.xml")
public class PayApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PayApplication.class);
    }

    @Bean
    public EmbeddedServletContainerFactory getServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(8099);
        factory.setSessionTimeout(30, TimeUnit.MINUTES);
//        factory.addErrorPages();
        return factory;
    }

}
