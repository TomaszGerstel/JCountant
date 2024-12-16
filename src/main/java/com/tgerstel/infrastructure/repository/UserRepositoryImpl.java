package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.User;
import com.tgerstel.domain.UserRole;
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
    public User findUserByUsername(String username) {
        return userSpringRepository.findByUsername(username)
                .map(UserEntity::toUser)
                .orElse(null);
    }

    @Override
    public Optional<UserRole> findRoleByName(String name) {
        return userRoleSpringRepository.findByName(name).map(UserRoleEntity::toUserRole);
    }

    @Override
    public void save(User user) {
        userSpringRepository.save(new UserEntity(user));
    }
}
