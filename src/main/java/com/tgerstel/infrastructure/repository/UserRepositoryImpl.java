package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserSpringRepository userSpringRepository;
    private final UserRoleSpringRepository userRoleSpringRepository;

    public UserRepositoryImpl(UserSpringRepository userSpringRepository, UserRoleSpringRepository userRoleSpringRepository) {
        this.userSpringRepository = userSpringRepository;
        this.userRoleSpringRepository = userRoleSpringRepository;
    }

    @Override
    public User findUserByName(String username) {
        return userSpringRepository.findByUsername(username);
    }

    @Override
    public Optional<UserRole> findRoleByName(String name) {
        return userRoleSpringRepository.findByName(name);
    }

    @Override
    public User save(User user) {
        return userSpringRepository.save(user);
    }
}
