package com.tgerstel.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tgerstel.model.RegistrationUser;
import com.tgerstel.model.User;
import com.tgerstel.repository.UserRepository;
import com.tgerstel.service.UserService;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(path="api/register", produces=MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {
	
	private UserService userService;
	private PasswordEncoder passEncoder;
	
	public RegistrationController(UserService userService, PasswordEncoder passEncoder) {
		super();
		this.userService = userService;
		this.passEncoder = passEncoder;
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@RequestBody @Valid RegistrationUser user, Errors errors) {
		if (errors.hasErrors()) {	
			List<String> errorMessage = errors.getFieldErrors().stream()
					.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());			
			return ResponseEntity.unprocessableEntity().body(errorMessage);
		}
		if (userService.userExists(user)) {
			String mess = "this username already exists";
			return ResponseEntity.status(HttpStatus.CONFLICT).body(mess);
		}
		userService.saveUser(user, passEncoder);		
		return ResponseEntity.ok().build();		
	}
	
}
