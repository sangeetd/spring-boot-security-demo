package com.springsecurity.project.security;

public enum UserPermissions {
	USER_READ("user:read"),
	USER_WRITE("user:write"),
	ADMIN_READ("admin:read"),
	ADMIN_WRITE("admin:write");
	
	private final String permission;

	private UserPermissions(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}
	
	
}
