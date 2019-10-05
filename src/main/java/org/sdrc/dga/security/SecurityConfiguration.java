package org.sdrc.dga.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;

	@Autowired
	private void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(userAuthenticationProvider);
	}

	/*
	 * To alter any configuration related to WEB-Application please update the
	 * configuration. This Authentication provider internally manages the
	 * authentication creation mechanism, using the UserRoleFeaturePermissionMapping
	 * tables.
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.httpBasic().and().authorizeRequests().antMatchers("/error","/authenticate","/update","/submission","/health","/database/**").
		permitAll().anyRequest().authenticated();
		
		http.csrf().ignoringAntMatchers("/error","/authenticate","/update","/submission","/health","/database/**").csrfTokenRepository(this.getCsrfTokenRepository());
		http.formLogin().disable();
	
				

		http.logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID", "XSRF-TOKEN");

		http.sessionManagement().enableSessionUrlRewriting(false);
		http.sessionManagement().sessionFixation().newSession();
	}
	
	private CsrfTokenRepository getCsrfTokenRepository() {
	    CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
	    tokenRepository.setCookiePath("/");
	    return tokenRepository;
	}

}