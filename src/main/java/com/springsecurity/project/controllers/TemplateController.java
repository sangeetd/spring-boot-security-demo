package com.springsecurity.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {
	
	@GetMapping("login")
	public String login() {
		return "login";
	}
	
	@GetMapping("dashboard")
	public String dashboard() {
		return "dashboard";
	}

}
