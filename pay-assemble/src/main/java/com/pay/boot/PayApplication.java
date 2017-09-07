package com.pay.boot;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
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
@EnableAutoConfiguration(exclude = BatchAutoConfiguration.class)
@ImportResource("classpath:spring-base.xml")
public class PayApplication extends SpringBootServletInitializer {

    private static final String HTTP_URL_PATTERNS[] = {
            "/static/*",
            "/templates/*"
    };

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PayApplication.class);
    }

    @Bean
    public EmbeddedServletContainerFactory getServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                //配置不需要重定向的地址
                SecurityConstraint noneSecurity = new SecurityConstraint();
                noneSecurity.setUserConstraint("NONE");
                SecurityCollection noneCollection = new SecurityCollection();
                noneCollection.addPattern("/static/*");
                noneCollection.addPattern("/templates/*");
                noneSecurity.addCollection(noneCollection);
                context.addConstraint(noneSecurity);
                //配置http重定向到https
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
            }
        };
        factory.setPort(8099);
        factory.setSessionTimeout(30, TimeUnit.MINUTES);
        factory.addAdditionalTomcatConnectors(createHttpConnector());
//        factory.addErrorPages();
        return factory;
    }

    /**
     * 配置支持http
     * @return
     */
    private Connector createHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(8099);
        //http请求重定向到https
        connector.setRedirectPort(8443);
        return connector;
    }

}
