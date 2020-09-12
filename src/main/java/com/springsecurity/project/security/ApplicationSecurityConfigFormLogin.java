package com.springsecurity.project.security;

import java.util.concurrent.TimeUnit;

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
public class ApplicationSecurityConfigFormLogin extends WebSecurityConfigurerAdapter {

	private PasswordEncoder passwordEncoder;

	@Autowired
	public ApplicationSecurityConfigFormLogin(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
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

	// this will provide custom-username/password/roles based authentication to our
	// application
	// for sake of example the username/password is hardcoded string but it could be
	// fetched from database
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		// TODO Auto-generated method stub
		UserDetails someUser = User.builder().username("username from db")
				// .password("password from db") //spring security user requird encoded password
				// providing plain string won't work
				// (java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for
				// the id "null")

				// solution to above
				.password(passwordEncoder.encode("password from db"))

				.roles("USER").build();

		// UserDetailsService is an interface of spring security framework and
		// InMemoryUserDetailsManager is a concrete class implementing this interface
		// this takes list<UserDetails>
		return new InMemoryUserDetailsManager(someUser);

	}

}