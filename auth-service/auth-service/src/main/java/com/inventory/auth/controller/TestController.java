package com.inventory.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
	
	@GetMapping("/test")
	public String test()
	{
		return "Protected API Test";
	}
	
	@GetMapping("/secure")
	public String secure() {
	    return "SECURE";
	}

}
