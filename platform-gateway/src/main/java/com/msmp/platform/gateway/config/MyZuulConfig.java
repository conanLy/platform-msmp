package com.msmp.platform.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyZuulConfig {

    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;


    @Bean
    public MyRouteLocator routeLocator() {
        MyRouteLocator routeLocator = new MyRouteLocator(this.server.getServletPrefix(), this.zuulProperties);
        return routeLocator;
    }
}
