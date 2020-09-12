package com.springsecurity.project.dbauthcustomization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DbUserDetailsService implements UserDetailsService{

	private final IDbUserDetailsFetcher userDetailsFetcher;
	
	@Autowired
	public DbUserDetailsService(@Qualifier("PostgresSQL") IDbUserDetailsFetcher userDetailsFetcher) {
		this.userDetailsFetcher = userDetailsFetcher;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userDetailsFetcher.findUserDetailsByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found..."));
	}

}
