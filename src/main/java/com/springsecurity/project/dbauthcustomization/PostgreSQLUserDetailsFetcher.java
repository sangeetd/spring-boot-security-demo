package com.springsecurity.project.dbauthcustomization;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.springsecurity.project.security.UserRoles;

@Repository("PostgresSQL")
public class PostgreSQLUserDetailsFetcher implements IDbUserDetailsFetcher{
	
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public PostgreSQLUserDetailsFetcher(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<DbUserDetails> findUserDetailsByUsername(String username) {
		// TODO Auto-generated method stub
		return codeLogicToFetchDataFromDatabase(username);
				
	}
	
	private Optional<DbUserDetails> codeLogicToFetchDataFromDatabase(String username){
		List<DbUserDetails> users=Lists.newArrayList(
				
				new DbUserDetails(
						"username1", 
						passwordEncoder.encode("password"),
						UserRoles.USER.getGrantedAuthorities(),
						true,
						true,
						true,
						true),
				
				new DbUserDetails(
						"username2", 
						passwordEncoder.encode("password"),
						UserRoles.ADMIN.getGrantedAuthorities(),
						true,
						true,
						true,
						true),
				
				new DbUserDetails(
						"username3", 
						passwordEncoder.encode("password"),
						UserRoles.ADMIN_TRAINEE.getGrantedAuthorities(),
						true,
						true,
						true,
						true)
				
				
				);
		
		return users
				.stream()
				.filter(userDetails -> username.equals(userDetails.getUsername()))
				.findFirst();
	}

}
