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

import com.tgerstel.domain.service.command.RegistrationCommand;
import com.tgerstel.domain.UserRole;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private DomainUserService userService;

	static PasswordEncoder passEncoder;
	static UserRole userRole;
	static RegistrationCommand registrationCommand;

	@BeforeAll
	static void prepareVariables() {
		passEncoder = new BCryptPasswordEncoder();
		userRole = new UserRole(1L, "USER", "USER");
		registrationCommand = new RegistrationCommand("Stan", "natan@gmail.com", "secret", 12);

	}

	@Test
	@DisplayName("should save and return proper User object")
	void testSaveUser() {

		// given
		ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
		Mockito.when(userRepository.findRoleByName("USER")).thenReturn(Optional.of(userRole));

		// when
		userService.saveUser(registrationCommand, passEncoder);

		Mockito.verify(userRepository).save(argumentCaptor.capture());

		// then
		User capturedUser = argumentCaptor.getValue();
		assertAll(
				() -> assertTrue(capturedUser.getRoles().contains(userRole)),
				() -> assertTrue(passEncoder.matches(registrationCommand.getPassword(), capturedUser.getPassword())),
				() -> assertEquals("Stan", capturedUser.getUsername()),
				() -> assertEquals(12, capturedUser.getLumpSumTaxRate())
		);
	}

	@Test
	@DisplayName("should throw NoSuchElementException if there is no default UserRole")
	void testSaveUserWhenNoUserRole() {
		// given
		Mockito.when(userRepository.findRoleByName("USER")).thenReturn(Optional.empty());

		// when
		// then
		Assertions.assertThatThrownBy(() -> userService.saveUser(registrationCommand, passEncoder))
				.isInstanceOf(NoSuchElementException.class);
		Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any());
	}

}
