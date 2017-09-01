package com.pay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by admin on 2017/7/25.
 * 事务管理器
 */
@Configuration()
public class TransactionManagerConfig {

    @Autowired(required = false)
    private DataSource dataSource;

    @Bean("transactionManager")
    public DataSourceTransactionManager getTxManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        transactionManager.setRollbackOnCommitFailure(true);
        transactionManager.setDefaultTimeout(60);
        return transactionManager;
    }

}
