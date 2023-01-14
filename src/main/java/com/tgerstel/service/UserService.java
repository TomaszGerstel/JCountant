package com.tgerstel.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tgerstel.model.RegistrationUser;
import com.tgerstel.model.User;
import com.tgerstel.model.UserRole;
import com.tgerstel.repository.UserRepository;
import com.tgerstel.repository.UserRoleRepository;

import lombok.Data;

@Data
@Service
public class UserService {
	
	private UserRepository userRepo;
	private UserRoleRepository userRoleRepo;
	
	public UserService(UserRepository userRepo, UserRoleRepository userRoleRepo) {
		super();
		this.userRepo = userRepo;
		this.userRoleRepo = userRoleRepo;
	}	

	public User saveUser(RegistrationUser user, PasswordEncoder passEncoder) {
		User newUser = user.toUser(passEncoder);
		Optional<UserRole> userRole = userRoleRepo.findByName("USER");
		userRole.ifPresentOrElse(role -> newUser.getRoles().add(role),
				() -> {
					throw new NoSuchElementException();
				});		
		return userRepo.save(newUser);
	}
	
	public boolean userExists(RegistrationUser user) {
		if(userRepo.findByUsername(user.getUsername()) != null) return true;
		return false;		
	}
}
