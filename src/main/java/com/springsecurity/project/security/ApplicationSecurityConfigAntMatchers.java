package com.springsecurity.project.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableWebSecurity
public class ApplicationSecurityConfigAntMatchers extends WebSecurityConfigurerAdapter{

	//simple basic auth
	//asking for security pssword for everyhing except (antMatchers(paramters...))
	//they will easily accessible without any authentication required.
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http
		.authorizeRequests()
		.antMatchers("/", "/css/*", "/js/*") //whitelisting of thing that doesn't need authentication for your application
		.permitAll() //and this will permit above parameters.
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
	}

	
	
}
