package com.tgerstel.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tgerstel.model.User;
import com.tgerstel.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private MyUserDetailsService userDetailsService;

	static User user;

	@BeforeAll
	static void prepareVariables() {
		user = new User("Stan", "natan@gmail.com", "secret", 12);
	}

	@Test
	@DisplayName("loadUserByUsernamer shoud return UserDetails with proper fields")
	void testLoadUserByUsername() {
		
		Mockito.when(userRepo.findByUsername("Stan")).thenReturn(user);

		UserDetails result = userDetailsService.loadUserByUsername("Stan");

		assertAll(				
				() -> assertNotNull(result),
				() -> assertEquals("Stan", result.getUsername()),		
				() -> assertEquals("secret", result.getPassword())
		);
	}

	@Test
	@DisplayName("loadUserByUsernamer shoud return UsernameNotFoundException if there is no User found")
	void testLoadUserByUsername2() {

		Mockito.when(userRepo.findByUsername("Stan")).thenReturn(null);

		Assertions.assertThatThrownBy(() -> userDetailsService.loadUserByUsername("Stan"))
				.isInstanceOf(UsernameNotFoundException.class);
		Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any());
	}

}
