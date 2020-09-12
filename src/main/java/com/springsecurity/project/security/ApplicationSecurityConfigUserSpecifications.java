package com.springsecurity.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//@Configuration
//@EnableWebSecurity
public class ApplicationSecurityConfigUserSpecifications extends WebSecurityConfigurerAdapter{

	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public ApplicationSecurityConfigUserSpecifications(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}


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

	
	//this will provide custom-username/password/roles based authentication to our application
	//for sake of example the username/password is hardcoded string but it could be 
	//fetched from database 
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		// TODO Auto-generated method stub
		UserDetails someUser=User.builder()
				.username("username from db")
				//.password("password from db") //spring security user requird encoded password
				//providing plain string won't work
				//(java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null")
				
				//solution to above 
				.password(passwordEncoder.encode("password from db"))
				
				.roles("USER")
				.build();
		
		//UserDetailsService is an interface of spring security framework and 
		//InMemoryUserDetailsManager is a concrete class implementing this interface
		//this takes list<UserDetails>
		return new InMemoryUserDetailsManager(
				someUser
				);
		
	}

	
	
}