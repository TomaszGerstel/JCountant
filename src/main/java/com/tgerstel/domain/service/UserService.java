package com.tgerstel.domain.service;

import com.tgerstel.domain.service.command.RegistrationCommand;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {

    void saveUser(RegistrationCommand user, PasswordEncoder passEncoder);
    boolean userExists(RegistrationCommand user);
}
