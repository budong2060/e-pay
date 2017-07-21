package com.pay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by admin on 2017/7/21.
 */
public class WebAppInitializer implements WebApplicationInitializer {

    @Autowired
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
        dispatcher.addMapping("*.html");
        dispatcher.addMapping("*.pdf");
        //Enable JSON response
        dispatcher.addMapping("*.json");
        dispatcher.addMapping("*.jsp");

    }

    private WebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context =new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        return context;
    }

}