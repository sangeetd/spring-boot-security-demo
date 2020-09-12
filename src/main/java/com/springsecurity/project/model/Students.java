package com.springsecurity.project.model;

public class Students {

	private Integer id;
	private String name;
	
	public Students(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	};
	
	
	
}
