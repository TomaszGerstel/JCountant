package com.tgerstel.domain.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tgerstel.domain.RegistrationUser;
import com.tgerstel.domain.UserRole;

import lombok.Data;

@Data
public class DomainUserService implements UserService {
	
	private UserRepository userRepository;

	public DomainUserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}	

	public void saveUser(RegistrationUser user, PasswordEncoder passEncoder) {
		User newUser = user.toUser(passEncoder);
		Optional<UserRole> userRole = userRepository.findRoleByName("USER");
		userRole.ifPresentOrElse(role -> newUser.getRoles().add(role),
				() -> {
					throw new NoSuchElementException();
				});
		userRepository.save(newUser);
	}
	
	public boolean userExists(RegistrationUser user) {
		if(userRepository.findUserByUsername(user.getUsername()) != null) return true;
		return false;		
	}
}
