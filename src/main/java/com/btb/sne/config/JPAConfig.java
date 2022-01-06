package com.btb.sne.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class JPAConfig {

    @Bean("jpa.transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource);
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }
}
