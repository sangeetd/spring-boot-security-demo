package com.springsecurity.project.security;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.springsecurity.project.dbauthcustomization.DbUserDetailsService;
import com.springsecurity.project.jwt.JwtTokenVerifier;
import com.springsecurity.project.jwt.JwtUsernameAndPasswordAuthFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfigJWT extends WebSecurityConfigurerAdapter {

	private PasswordEncoder passwordEncoder;
	private DbUserDetailsService userDetailsService;

	@Autowired
	public ApplicationSecurityConfigJWT(PasswordEncoder passwordEncoder,
			DbUserDetailsService userDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService=userDetailsService;
	}

	// Instead of using basic auth we can use form based login
	// just by replacing httpBasic() with formLogin()
	// asking for security pssword for everyhing except (antMatchers(paramters...))
	// they will easily accessible without any authentication required.

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.csrf().disable()
		
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(new JwtUsernameAndPasswordAuthFilter(authenticationManager()))
				.addFilterAfter(new JwtTokenVerifier(), JwtUsernameAndPasswordAuthFilter.class)
				.authorizeRequests()
				
				//this match the rest api request from client that has /api/ in it
				//and that client's authentication has role of 'user' then only request can be forwarded
				.antMatchers("/api/**").hasRole(UserRoles.USER.name())
				//in the same way we can create apis that are only consumable to the client 
				//that has some specific role assigned to them via their authentication
				
				.antMatchers("/", "index", "/css/*", "/js/*") // whitelisting of thing
																										// your application
				.permitAll() // and this will permit above parameters.
				.anyRequest()
				.authenticated();

	}

	@Bean
	public DaoAuthenticationProvider getDaoAuthenticationProvide() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.authenticationProvider(getDaoAuthenticationProvide());
	}
	
	
	
	
	
}