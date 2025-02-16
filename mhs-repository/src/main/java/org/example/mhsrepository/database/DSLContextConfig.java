package org.example.mhsrepository.database;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DSLContextConfig {
    private final HikariDataSource dataSource;

    public DSLContextConfig(@Qualifier("dbCore") HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "contextCore")
    @Primary
    public DSLContext dslContext() {
        Settings settings = new Settings().withRenderSchema(false);
        return DSL.using(dataSource, SQLDialect.POSTGRES, settings);
    }
}

