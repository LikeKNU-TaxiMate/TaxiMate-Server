package com.woopaca.taximate.core.api.common.config;

import com.woopaca.taximate.core.api.auth.interceptor.AuthenticateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticateInterceptor authenticateInterceptor;

    public WebMvcConfiguration(AuthenticateInterceptor authenticateInterceptor) {
        this.authenticateInterceptor = authenticateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticateInterceptor);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
