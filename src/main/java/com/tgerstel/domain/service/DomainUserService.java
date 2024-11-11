package com.tgerstel.domain.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tgerstel.domain.RegistrationUser;
import com.tgerstel.infrastructure.repository.User;
import com.tgerstel.infrastructure.repository.UserRole;
import com.tgerstel.infrastructure.repository.UserSpringRepository;
import com.tgerstel.infrastructure.repository.UserRoleSpringRepository;

import lombok.Data;

@Data
@Service
public class DomainUserService implements UserService {
	
	private UserSpringRepository userRepo;
	private UserRoleSpringRepository userRoleRepo;
	
	public DomainUserService(UserSpringRepository userRepo, UserRoleSpringRepository userRoleRepo) {
		super();
		this.userRepo = userRepo;
		this.userRoleRepo = userRoleRepo;
	}	

	public void saveUser(RegistrationUser user, PasswordEncoder passEncoder) {
		User newUser = user.toUser(passEncoder);
		Optional<UserRole> userRole = userRoleRepo.findByName("USER");
		userRole.ifPresentOrElse(role -> newUser.getRoles().add(role),
				() -> {
					throw new NoSuchElementException();
				});
		userRepo.save(newUser);
	}
	
	public boolean userExists(RegistrationUser user) {
		if(userRepo.findByUsername(user.getUsername()) != null) return true;
		return false;		
	}
}
