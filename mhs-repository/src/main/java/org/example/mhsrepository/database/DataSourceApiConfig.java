package org.example.mhsrepository.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties("spring.datasource")
public class DataSourceApiConfig extends HikariConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dbCore")
    @Primary
    public HikariDataSource dataSource() {
        logger.info("Datasource: {}, username: {}", this.getJdbcUrl(), this.getUsername());
        return new HikariDataSource(this);
    }
}
