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
@EnableJpaRepositories(entityManagerFactoryRef = "DevinfoEntityManagerFactory", transactionManagerRef = "DevinfoTransactionManager", basePackages = {
		"org.sdrc.devinfo.repository.springdatajpa" })
public class DevinfoDataSourceConfig {

	@Autowired
	private Environment env;

	@Bean(name = "DevinfoDataSource")
	@ConfigurationProperties(prefix = "spring.datasourceDevinfo.hikari")
	public DataSource dataSource() {
		return devInfoDataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean
	@ConfigurationProperties("spring.datasourceDevinfo")
	public DataSourceProperties devInfoDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "DevinfoDataSourceHikari")
	@ConfigurationProperties(prefix = "spring.datasourceDevinfo.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

//  @Bean
//  public DataSource devinfoSataSourceConfig() {
//      return new HikariDataSource(hikariConfig());
//  }

	@Bean(name = "DevinfoEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean devinfoEntityManagerFactory() {

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan("org.sdrc.devinfo.domain");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setPersistenceUnitName("DevinfoSource");
		em.setJpaProperties(additionalProperties());
		return em;
	
		
//		return builder.dataSource(dataSource).packages("org.sdrc.devinfo.domain").persistenceUnit("DevinfoSource")
//				.build();
	}

	@Bean(name = "DevinfoTransactionManager")
	public PlatformTransactionManager devinfoTransactionManager(
			@Qualifier("DevinfoEntityManagerFactory") EntityManagerFactory devinfoEntityManagerFactory) {
		return new JpaTransactionManager(devinfoEntityManagerFactory);
	}

	Properties additionalProperties() {
		Properties hibernateProperties = new Properties();

		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
		hibernateProperties.setProperty("hibernate.dialect",
				env.getProperty("spring.jpa.properties.hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.show_sql",
				env.getProperty("spring.jpa.properties.hibernate.show_sql"));
		return hibernateProperties;
	}

}