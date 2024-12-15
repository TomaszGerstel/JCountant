package com.tgerstel.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tgerstel.domain.RegistrationUser;
import com.tgerstel.infrastructure.repository.UserRole;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private DomainUserService userService;

	static PasswordEncoder passEncoder;
	static UserRole userRole;
	static RegistrationUser registrationUser;

	@BeforeAll
	static void prepareVariables() {
		passEncoder = new BCryptPasswordEncoder();
		userRole = new UserRole();
		userRole.setName("USER");
		registrationUser = new RegistrationUser("Stan", "natan@gmail.com", "secret", 12);

	}

	@Test
	@DisplayName("saveUser should return (and pass to repository) proper User object")
	void testSaveUser() {

		ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
		Mockito.when(userRepo.findRoleByName("USER")).thenReturn(Optional.of(userRole));

		userService.saveUser(registrationUser, passEncoder);

		Mockito.verify(userRepo).save(argumentCaptor.capture());
		User capturedUser = argumentCaptor.getValue();

		assertAll(

				() -> assertTrue(capturedUser.getRoles().contains(userRole)),
				() -> assertTrue(passEncoder.matches(registrationUser.getPassword(), capturedUser.getPassword())),
				() -> assertEquals("Stan", capturedUser.getUsername()),
				() -> assertEquals(12, capturedUser.getLumpSumTaxRate())

		);
	}

	@Test
	@DisplayName("saveUser should return NoSuchElementException if there is no UserRole")
	void testSaveUser2() {

		Mockito.when(userRepo.findRoleByName("USER")).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> userService.saveUser(registrationUser, passEncoder))
				.isInstanceOf(NoSuchElementException.class);
		Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any());
	}

}
