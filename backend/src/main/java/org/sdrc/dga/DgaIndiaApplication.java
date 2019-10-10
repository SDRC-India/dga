
package org.sdrc.dga;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.sdrc.dga.service.DistrictHealthActionPlanServiceImpl;
import org.sdrc.dga.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Harsh Pratyush
 * @email harsh@sdrc.co.in
 * @create date 2018-12-07 17:16:13
 * @modify date 2018-12-07 17:16:13
 * @desc [description]
*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableCaching
public class DgaIndiaApplication extends SpringBootServletInitializer {
	
	@Autowired
	private Environment environment;
	
	private static final Logger logger = LoggerFactory.getLogger(DgaIndiaApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DgaIndiaApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(DgaIndiaApplication.class, args);
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		
		return messageSource;
		
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@PostConstruct
	public void init(){
		try {
			if(!Paths.get(environment.getProperty(Constants.DGA_ROOT_PATH)).toFile().exists())
				Files.createDirectory(Paths.get(environment.getProperty(Constants.DGA_ROOT_PATH)));
			if(!Paths.get(environment.getProperty(Constants.DGA_DISTRICT_HEALTH_ACTION_PLAN_PATH)).toFile().exists())
				Files.createDirectory(Paths.get(environment.getProperty(Constants.DGA_DISTRICT_HEALTH_ACTION_PLAN_PATH)));
			if(!Paths.get(environment.getProperty(Constants.DGA_LAQSHYA)).toFile().exists())
				Files.createDirectory(Paths.get(environment.getProperty(Constants.DGA_LAQSHYA)));
		} catch (Exception e) {
			logger.error("failed to create directories on application startup", e);
		}
	}
}
