package com.springsecurity.project.security;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum UserRoles {
	USER(Sets.newHashSet()),
	ADMIN(Sets.newHashSet(
			UserPermissions.USER_READ, 
			UserPermissions.USER_WRITE, 
			UserPermissions.ADMIN_READ, 
			UserPermissions.ADMIN_WRITE)),
	ADMIN_TRAINEE(Sets.newHashSet(
			UserPermissions.USER_READ,  
			UserPermissions.ADMIN_READ));
	
	private final Set<UserPermissions> permission;
	
	UserRoles(Set<UserPermissions> permission){
		this.permission=permission;
	}

	public Set<UserPermissions> getPermission() {
		return permission;
	}
	
	public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
		Set<SimpleGrantedAuthority> authority = getPermission().stream()
		.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
		.collect(Collectors.toSet());
		authority.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
		return authority;
		
	}
	
}
