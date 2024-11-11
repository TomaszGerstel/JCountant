package com.tgerstel.domain.service;

import com.tgerstel.domain.RegistrationUser;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {

    void saveUser(RegistrationUser user, PasswordEncoder passEncoder);
    boolean userExists(RegistrationUser user);
}
