package com.springsecurity.project.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurity.project.model.Students;

@RestController
@RequestMapping("admin/api/v1")
public class AdminAPIController {


	private List<Students> studentsList=Arrays.asList(
			new Students(1, "sangeet 1"),
			new Students(2, "sangeet 2"),
			new Students(3, "sangeet 3")
			
			);
	
	//READ-permission
	@GetMapping("/getAll")
	public List<Students> getAllStudent(){
		return studentsList;
	}
	
	//WRITE-permission
	@PostMapping("/addStudent")
	public void addStudent(@RequestBody Students student) {
		System.out.println(student+" added");
	}
	
	//WRITE-permission
	@DeleteMapping("/deleteStudent/{id}")
	public void removeStudent(@PathVariable Integer id) {
		System.out.println(id+" removed");
	}
	
	//WRITE-permission
	@PutMapping("/updateStudent/{id}")
	public void updateStudent(@PathVariable Integer id) {
		System.out.println(id+" updated");
	}
	
}
