package com.example.catalog.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class DataSourceSelector {

    @Value("${datasource.type:json}") // Default to 'json' if not specified
    private String dataSourceType;

    @Bean
    public DataSourceService dataSourceService(JSONDataSourceService jsonDataSourceService) {
        if ("json".equalsIgnoreCase(dataSourceType)) {
            return jsonDataSourceService;
        } else if ("database".equalsIgnoreCase(dataSourceType)) {
            return jsonDataSourceService;
        } else {
            throw new IllegalArgumentException("Invalid data source type: " + dataSourceType);
        }
    }
}
