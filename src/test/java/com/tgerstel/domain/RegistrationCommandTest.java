package com.tgerstel.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.tgerstel.domain.service.command.RegistrationCommand;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class RegistrationCommandTest {

    @Test
    void testToUser() {
        // given
        RegistrationCommand userRegistration = new RegistrationCommand("Al", "al@gmail.com", "secret", 12);
        PasswordEncoder passEncoder = new BCryptPasswordEncoder();

        // when
        User userBase = userRegistration.toUser(passEncoder);

        // then
        assertAll(
                () -> assertTrue(passEncoder.matches(userRegistration.getPassword(), userBase.getPassword())),
                () -> assertEquals("Al", userBase.getUsername()),
                () -> assertEquals(12, userBase.getLumpSumTaxRate())
        );
    }

}
