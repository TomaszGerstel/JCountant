package com.tgerstel.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class RegistrationUserTest {

	@Test
	void testToUser() {
		RegistrationUser userRegistration = new RegistrationUser("Al", "al@gmail.com", "secret", 12);
		PasswordEncoder passEncoder = new  BCryptPasswordEncoder();		
		
		User userBase = userRegistration.toUser(passEncoder);
		
		assertAll(
		
		() -> assertTrue(passEncoder.matches(userRegistration.getPassword(), userBase.getPassword())),
		() -> assertEquals("Al", userBase.getUsername()),
		() -> assertEquals(12, userBase.getLumpSumTaxRate())
		
		);
	}

}
