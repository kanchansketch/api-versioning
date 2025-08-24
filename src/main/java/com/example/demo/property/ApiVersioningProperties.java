package com.example.demo.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apiversioning")
public class ApiVersioningProperties {

    public enum Strategy {
        PATH, HEADER, QUERY
    }

    private Strategy strategy = Strategy.PATH; // default
    private String headerName = "X-API-Version";
    private String queryParam = "version";
    private String basePath = "/api"; // configurable prefix

    // getters & setters
    public Strategy getStrategy() { return strategy; }
    public void setStrategy(Strategy strategy) { this.strategy = strategy; }

    public String getHeaderName() { return headerName; }
    public void setHeaderName(String headerName) { this.headerName = headerName; }

    public String getQueryParam() { return queryParam; }
    public void setQueryParam(String queryParam) { this.queryParam = queryParam; }

    public String getBasePath() { return basePath; }
    public void setBasePath(String basePath) { this.basePath = basePath; }
}
