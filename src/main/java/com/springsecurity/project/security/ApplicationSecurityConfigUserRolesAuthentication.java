package com.springsecurity.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
public class ApplicationSecurityConfigUserRolesAuthentication  extends WebSecurityConfigurerAdapter{

	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public ApplicationSecurityConfigUserRolesAuthentication(PasswordEncoder passwordEncoder) {
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
		
		//whitelisting of thing that doesn't need authentication for your application
		.antMatchers("/", "/css/*", "/js/*").permitAll() 
		//and this will permit above parameters.
		
		//this match the rest api request from client that has /api/ in it
		//and that client's authentication has role of 'user' then only request can be forwarded
		.antMatchers("/api/**").hasRole(UserRoles.USER.name())
		//in the same way we can create apis that are only consumable to the client 
		//that has some specific role assigned to them via their authentication
		
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
				
				.roles(UserRoles.USER.name())
				.build();
		
		
		UserDetails adminUser=User.builder()
				.username("AdminUsername")
				//.password("AdminPassword") //spring security user requird encoded password
				//providing plain string won't work
				//(java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null")
				
				//solution to above 
				.password(passwordEncoder.encode("AdminPassword"))
				
				.roles(UserRoles.ADMIN.name())
				.build();
		
		//UserDetailsService is an interface of spring security framework and 
		//InMemoryUserDetailsManager is a concrete class implementing this interface
		//this takes list<UserDetails>
		return new InMemoryUserDetailsManager(
				someUser,
				adminUser
				);
		
	}

	
	
}