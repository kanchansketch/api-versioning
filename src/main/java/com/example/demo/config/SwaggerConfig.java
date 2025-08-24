package com.example.demo.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi apiV1() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiV2() {
        return GroupedOpenApi.builder()
                .group("v2")
                .pathsToMatch("/api/v2/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiV3() {
        return GroupedOpenApi.builder()
                .group("v3")
                .pathsToMatch("/api/v3/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiV4() {
        return GroupedOpenApi.builder()
                .group("v4")
                .pathsToMatch("/api/v4/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiAll() {
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/**")
                .build();
    }
}
