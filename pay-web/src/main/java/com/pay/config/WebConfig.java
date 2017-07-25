package com.pay.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pay.common.PayHandlerExceptionResolver;
import com.pay.intercepter.AllInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import javax.servlet.Filter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Halbert on 2017/7/2.
 * WebMvc配置项
 */
@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

//    @Bean
//    public InternalResourceViewResolver viewResolver() {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix("/WEB-INF/jsp/");
//        resolver.setSuffix(".jsp");
//        resolver.setContentType("text/html; charset=UTF-8");
//        return resolver;
//    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //vm
        VelocityLayoutViewResolver vmResolver = new VelocityLayoutViewResolver();
        vmResolver.setSuffix(".vm");
        vmResolver.setToolboxConfigLocation("/config/toolbox.xml");
        vmResolver.setContentType("text/html; charset=UTF-8");
        vmResolver.setViewNames("*.vm");
        registry.viewResolver(vmResolver);

        //jsp
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setContentType("text/html; charset=UTF-8");
        registry.viewResolver(resolver);
    }

    @Bean
    public VelocityConfigurer viewResolver() {
        VelocityConfigurer configurer = new VelocityConfigurer();
        configurer.setResourceLoaderPath("classpath:/templates/");
        Map<String, Object> velocityProperties = new HashMap<String, Object>();
        velocityProperties.put("input.encoding", "UTF-8");
        velocityProperties.put("output.encoding", "UTF-8");
        velocityProperties.put("parser.pool.size", 100);
        velocityProperties.put("velocimacro.library.autoreload", true);
        velocityProperties.put("velocimacro.context.localscope", true);
        configurer.setVelocityPropertiesMap(velocityProperties);
        return configurer;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//        registry.addResourceHandler("/templates/*.html", "/templates/*.htm", "/templates/*.jsp").addResourceLocations("/templates/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        ObjectMapper objectMapper = builder.build();
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        converters.add(converter);
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new PayHandlerExceptionResolver());
        super.configureHandlerExceptionResolvers(exceptionResolvers);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new AllInterceptor());
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
