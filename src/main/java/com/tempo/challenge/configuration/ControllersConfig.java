package com.tempo.challenge.configuration;

import com.tempo.challenge.controllers.EndpointInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ControllersConfig implements WebMvcConfigurer {

    private EndpointInterceptor interceptor;

    @Autowired
    public ControllersConfig(final EndpointInterceptor interceptor) {
        this.interceptor = interceptor;
    }
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
