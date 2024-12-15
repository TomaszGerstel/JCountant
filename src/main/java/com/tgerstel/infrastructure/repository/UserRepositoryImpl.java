package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.User;
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
        UserEntity found = userSpringRepository.findByUsername(username);
        return found == null ? null : found.toUser();
    }

    @Override
    public Optional<UserRole> findRoleByName(String name) {
        return userRoleSpringRepository.findByName(name);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = new UserEntity(user);
        return userSpringRepository.save(userEntity).toUser();
    }
}
