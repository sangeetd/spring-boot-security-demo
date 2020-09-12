package com.springsecurity.project.jwt;

public enum JwtKeyEnum {

	KEY("SomeKeyHere");
	
	private String key;
	
	JwtKeyEnum(String k){
		this.key=k;
	}
	
}
