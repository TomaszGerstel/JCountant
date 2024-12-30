package com.tgerstel.infrastructure.login;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.UserRepository;
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

import java.util.Set;


@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private MyUserDetailsService userDetailsService;

	static User user;

	@BeforeAll
	static void prepareVariables() {
		user = new User(1L,"Stan", "natan@gmail.com", "secret", 12, Set.of());
	}

	@Test
	@DisplayName("should return UserDetails with proper fields")
	void testLoadUserByUsername() {
		// given
		Mockito.when(userRepository.findUserByUsername("Stan")).thenReturn(user);

		// when
		UserDetails result = userDetailsService.loadUserByUsername("Stan");

		// then
		assertAll(				
				() -> assertNotNull(result),
				() -> assertEquals("Stan", result.getUsername()),		
				() -> assertEquals("secret", result.getPassword())
		);
	}

	@Test
	@DisplayName("should return UsernameNotFoundException if there is no User found")
	void testLoadUserByUsernameWhenNoUser() {

		// given
		Mockito.when(userRepository.findUserByUsername("Stan")).thenReturn(null);

		// when
		// then
		Assertions.assertThatThrownBy(() -> userDetailsService.loadUserByUsername("Stan"))
				.isInstanceOf(UsernameNotFoundException.class);
		Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any());
	}

}
