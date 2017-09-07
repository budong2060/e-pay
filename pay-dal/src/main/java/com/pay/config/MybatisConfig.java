package com.pay.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.autoconfigure.transaction.TransactionProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by admin on 2017/7/4.
 */
@Configuration()
@PropertySource("classpath:jdbc-${spring.active.profile}.properties")
@MapperScan("com.pay.mybatis")
public class MybatisConfig {

    @Value("${db.username}")
    private String userName;

    @Value("${db.password}")
    private String userPwd;

    @Value("${db.mysqlUrl}")
    private String url;

    @Value("${db.mysqlClassName}")
    private String driverClassName;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.type(DruidDataSource.class);
        builder.username(userName);
        builder.password(userPwd);
        builder.url(url);
        builder.driverClassName(driverClassName);
        DruidDataSource dataSource = (DruidDataSource) builder.build();
        dataSource.setInitialSize(10);
        dataSource.setMaxActive(80);
        dataSource.setMaxWait(500);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setValidationQuery("select 1 from dual");
        return dataSource;
    }

    /**
     * mybatis配置读取
     * 通过MybatisAutoConfiguration读取相关配置
     * @return
     */
//    @Primary
//    @Bean
//    public MybatisProperties getProperties() {
//        MybatisProperties properties = new MybatisProperties();
////        properties.setExecutorType(ExecutorType.SIMPLE);
//        properties.setTypeAliasesPackage("com.pay.domain");
//        properties.setConfigLocation("classpath:mybatis/mybatis-config.xml");
//        properties.setMapperLocations(new String[] { "classpath*:sqlmap/*Mapper.xml" });
//        return properties;
//    }

    /**
     * 事务管理
     * DataSourceTransactionManagerAutoConfiguration
     * @return
     */
//    @Primary
//    @Bean
//    public TransactionProperties getTxProperties() {
//        TransactionProperties txProperties = new TransactionProperties();
//        txProperties.setDefaultTimeout(60); //设置事务过期事件
//        txProperties.setRollbackOnCommitFailure(true); //设置事务提交失败回滚
//        return txProperties;
//    }

    @Primary
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setTypeAliasesPackage("com.pay.domain");
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:sqlmap/*Mapper.xml"));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
