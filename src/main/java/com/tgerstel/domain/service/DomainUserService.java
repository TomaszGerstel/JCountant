package com.tgerstel.domain.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tgerstel.domain.service.command.RegistrationCommand;
import com.tgerstel.domain.UserRole;

public class DomainUserService implements UserService {
	
	private final UserRepository userRepository;

	public DomainUserService(final UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}	

	public void saveUser(final RegistrationCommand user, final PasswordEncoder passEncoder) {
		final User newUser = user.toUser(passEncoder);
		final Optional<UserRole> userRole = userRepository.findRoleByName("USER");
		userRole.ifPresentOrElse(role -> newUser.getRoles().add(role),
				() -> {
					throw new NoSuchElementException();
				});
		userRepository.save(newUser);
	}
	
	public boolean userExists(final RegistrationCommand user) {
        return userRepository.findUserByUsername(user.getUsername()) != null;
    }
}
