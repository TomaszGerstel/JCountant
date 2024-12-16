package com.tgerstel.domain.repository;

import com.tgerstel.domain.User;
import com.tgerstel.domain.UserRole;

import java.util.Optional;

public interface UserRepository {

    User findUserByUsername(String username);

    Optional<UserRole> findRoleByName(String name);

    void save(User user);
}
