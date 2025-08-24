package com.example.demo.config;

import com.example.demo.property.ApiVersioningProperties;
import com.example.demo.versioning.ApiVersionRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableConfigurationProperties(ApiVersioningProperties.class)
public class WebConfig implements WebMvcRegistrations {

    private final ApiVersioningProperties properties;

    public WebConfig(ApiVersioningProperties properties) {
        this.properties = properties;
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        ApiVersionRequestMappingHandlerMapping mapping =
                new ApiVersionRequestMappingHandlerMapping(properties);
        mapping.setOrder(0); // ensure it runs before Spring’s default mapping
        return mapping;
    }
}
