package com.tgerstel.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@CrossOrigin(origins="*")
public class LoginController {
	
	@PostMapping("/login")
	 public Principal user(Principal user) {
	    return user;
	  }
}
