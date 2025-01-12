package com.tgerstel.domain.service;

import com.tgerstel.JCountantApplication;
import com.tgerstel.configuration.JCountantPostgreSqlContainer;
import com.tgerstel.domain.service.command.RegistrationCommand;
import com.tgerstel.infrastructure.repository.UserEntity;
import com.tgerstel.infrastructure.repository.UserSpringRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = JCountantApplication.class)
@ActiveProfiles("it")
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSpringRepository userSpringRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Container
    public static PostgreSQLContainer<JCountantPostgreSqlContainer> postgreSQLContainer =
            JCountantPostgreSqlContainer.getInstance();

    @Test
    @Transactional
    public void shouldRegisterUser() {
        // given
        String userName = "testUser";
        RegistrationCommand command = new RegistrationCommand( userName, "test@email.com", "hardpass", 9);

        // when
        userService.saveUser(command, passwordEncoder);

        //then
        Optional<UserEntity> savedUser = userSpringRepository.findByUsername(userName);

        assertThat(savedUser.isPresent()).isTrue();
        assertThat(savedUser.get().getUsername()).isEqualTo(userName);
    }

}
