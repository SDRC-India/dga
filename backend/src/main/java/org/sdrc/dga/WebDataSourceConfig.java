/**
 * 
 */
package org.sdrc.dga;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in) Created on 01-Dec-2018
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "webEntityManagerFactory", transactionManagerRef = "webTransactionManager", basePackages = {
		"org.sdrc.dga.repository.springdatajpa" })
public class WebDataSourceConfig {

	@Autowired
	private Environment env;

	@Primary
	@Bean(name = "webDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource webDataSource() {

		return webDataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties webDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "webHikariSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        return new HikariDataSource(hikariConfig());
//    }

	@Primary
	@Bean(name = "webEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean webEntityManagerFactoryConfig() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(webDataSource());
		em.setPackagesToScan("org.sdrc.dga.domain");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setPersistenceUnitName("webSource");
		em.setJpaProperties(additionalProperties());
		return em;
	}

	@Primary
	@Bean(name = "webTransactionManager")
	public PlatformTransactionManager webTransactionManagerConfig(
			@Qualifier("webEntityManagerFactory") EntityManagerFactory webEntityManagerFactory) {
		return new JpaTransactionManager(webEntityManagerFactory);
	}

	Properties additionalProperties() {
		Properties hibernateProperties = new Properties();

		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		hibernateProperties.setProperty("hibernate.dialect",
				env.getProperty("spring.jpa.properties.hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.show_sql",
				env.getProperty("spring.jpa.properties.hibernate.show_sql"));
//
//		hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "true");
//		hibernateProperties.setProperty("hibernate.cache.use_query_cache", "true");
//		hibernateProperties.setProperty("hibernate.cache.region.factory_class",
//				"org.hibernate.cache.ehcache.EhCacheRegionFactory");
		return hibernateProperties;
	}

}