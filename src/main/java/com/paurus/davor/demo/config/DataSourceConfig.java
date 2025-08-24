package com.paurus.davor.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "batchDataSource")
    @ConfigurationProperties("batch.datasource")
    public DataSource batchDataSource() {
        return new HikariDataSource();
    }
}
