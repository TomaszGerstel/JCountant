package com.tgerstel.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tgerstel.model.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long>{


	Optional<UserRole> findByName(String name);

}
