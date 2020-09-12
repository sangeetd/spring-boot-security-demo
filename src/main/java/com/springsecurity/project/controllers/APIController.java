package com.springsecurity.project.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurity.project.model.Students;

@RestController
@RequestMapping("api/v1")
public class APIController {

	private List<Students> studentsList=Arrays.asList(
			new Students(1, "sangeet 1"),
			new Students(2, "sangeet 2"),
			new Students(3, "sangeet 3")
			
			);
	
	
	@GetMapping("/test/{testString}")
	public String testAPI(@PathVariable String testString) {
		return testString;
	}
	
	@GetMapping("/test2/{testString},{testString2}")
	public String testAPI2(@PathVariable String testString, @PathVariable String testString2) {
		return testString+" "+testString2;
	}
	
	@GetMapping("student/{id}")
	public Students findById(@PathVariable Integer id) {
		
		try {
			return studentsList.stream()
			.filter(student -> id.equals(student.getId()))
			.findFirst()
			.orElseThrow(() -> new Exception("Student id you requested for is not found"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	//in console after adding simple spring security dependency
	//boot gives you default security login page with defaylt generated password:
	//4fa7c2d7-01b2-4e1b-a232-e7116b42d236 
	//always generate new when you start the project
	
	
	
}
