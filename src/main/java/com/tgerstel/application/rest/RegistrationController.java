package com.tgerstel.application.rest;

import java.util.List;
import java.util.stream.Collectors;

import com.tgerstel.domain.service.UserService;
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

import com.tgerstel.domain.RegistrationUser;

import jakarta.validation.Valid;

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
			List<String> mess = List.of("this username already exists");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(mess);
		}
		userService.saveUser(user, passEncoder);		
		return ResponseEntity.ok().build();		
	}
	
}
