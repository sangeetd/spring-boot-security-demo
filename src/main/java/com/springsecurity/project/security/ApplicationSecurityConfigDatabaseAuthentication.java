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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.springsecurity.project.dbauthcustomization.DbUserDetailsService;

//@Configuration
//@EnableWebSecurity
public class ApplicationSecurityConfigDatabaseAuthentication extends WebSecurityConfigurerAdapter {

	private PasswordEncoder passwordEncoder;
	private DbUserDetailsService userDetailsService;

	@Autowired
	public ApplicationSecurityConfigDatabaseAuthentication(PasswordEncoder passwordEncoder,
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
				.authorizeRequests()
				
				//this match the rest api request from client that has /api/ in it
				//and that client's authentication has role of 'user' then only request can be forwarded
				.antMatchers("/api/**").hasRole(UserRoles.USER.name())
				//in the same way we can create apis that are only consumable to the client 
				//that has some specific role assigned to them via their authentication
				
				.antMatchers("/", "index", "/css/*", "/js/*") // whitelisting of thing
						
				// your application
				.permitAll() // and this will permit above parameters.
				.anyRequest().authenticated().and().formLogin() // this will enable form based login spring security
																// defult login page

				// to customize your login page
				// add below methods

				// this request for /login will be mapped in view controller
				// which returns your customized html login-page
				.loginPage("/login")

				// you can change the 'name: attribute' from your login.html page
				// similarly you can give here passwordParameter
				.usernameParameter("usernamexyz")

				// if you want to redirect to a certain page after successful login add below
				// methods also
				// otherwise it will automatically redirect to index.html
				.defaultSuccessUrl("/dashboard")

				.permitAll()

				// spring security provides 'Remember me' feature on login
				// with this the JSESSIONID can be retained in InMemoryDatabase of spring
				// default time-period expiry for remember me is set to 2 weeks
				// add below method to enable remember me
				.and().rememberMe()
				//if you have diff attribute name for your remember me checkbox 
				//you can declare it here just like above usernameParameter
				//.rememberMeParameter("rememberMe")

				// we can also set the expiry date to our own time by adding these methods
				// keys is with which sessionid is going to encrypted in the browser
				// if you don't give a key, spring will take its own.
				.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(20)).key("somesecurekeys")

				// adding logout feature to the formLogin
				.and().logout()
				// when the /logout is hit
				// you can see here in the dashboard.html page in the <form> logout
				//btn is redirecting to /logout url which matched below pattern
				.logoutUrl("/logout")
				// reset the httpsession
				.invalidateHttpSession(true)
				// clear the session cookies data
				.deleteCookies("JSESSIONID", "remember-me")
				// clear authentication related data
				.clearAuthentication(true)
				// after successful logout
				// open /login page so that one login again if he wants to.
				.logoutSuccessUrl("/login")
				.permitAll();

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