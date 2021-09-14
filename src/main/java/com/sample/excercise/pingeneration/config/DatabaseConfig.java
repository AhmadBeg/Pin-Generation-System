package com.sample.excercise.pingeneration.config;


import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConfig {

    @Autowired
    Environment env;
    
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    
    @Value("${spring.datasource.driver-class-name}")
    private String jdbcClassName;
    
    @Value("${spring.datasource.username}")
    private String userName;
    
    @Value("${spring.datasource.password}")
    private String pswd;
    
    @Bean(name = "PinDataSource")
    public DataSource primaryDataSource() {

	PoolProperties poolProperties = new PoolProperties();
	poolProperties.setUrl(jdbcUrl);
	poolProperties.setUsername(userName);
	poolProperties.setPassword(pswd);
	poolProperties.setDriverClassName(jdbcClassName);
	poolProperties.setTestOnBorrow(true);
	poolProperties.setValidationQuery("SELECT 1");
	poolProperties.setMinIdle(5);
	poolProperties.setMaxIdle(10);
	poolProperties.setJmxEnabled(true);
	poolProperties.setLogValidationErrors(true);
	poolProperties.setTimeBetweenEvictionRunsMillis(6000);
	return new DataSource(poolProperties);
    }

    @Bean(name = "PinJDBCTemplate")
    JdbcTemplate oraclejdbcTemplate(@Qualifier("PinDataSource") DataSource pinDb) {
	return new JdbcTemplate(pinDb);
    }
}
