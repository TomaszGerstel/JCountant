package com.tgerstel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.RegistrationUser;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.model.UserRole;
import com.tgerstel.repository.ReceiptRepository;
import com.tgerstel.repository.TransferRepository;
import com.tgerstel.repository.UserRepository;
import com.tgerstel.repository.UserRoleRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRoleRepository userRoleRepo;
	@Mock
	private TransferRepository transferRepo;
	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private UserService userService;

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
	@DisplayName("saveUser shoud return (and pass to repository) proper User object")
	void testSaveUser() {

		ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
		Mockito.when(userRoleRepo.findByName("USER")).thenReturn(Optional.of(userRole));

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
	@DisplayName("saveUser shoud return NoSuchElementException if there is no UserRole")
	void testSaveUser2() {

		Mockito.when(userRoleRepo.findByName("USER")).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> userService.saveUser(registrationUser, passEncoder))
				.isInstanceOf(NoSuchElementException.class);
		Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any());
	}

}
