package com.tgerstel.domain.repository;

import com.tgerstel.infrastructure.repository.User;
import com.tgerstel.infrastructure.repository.UserRole;

import java.util.Optional;

public interface UserRepository {

    User findUserByName(String username);

    Optional<UserRole> findRoleByName(String name);

    User save(User user);
}
