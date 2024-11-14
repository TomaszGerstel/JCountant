package com.tgerstel.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRoleSpringRepository extends CrudRepository<UserRole, Long>{

	Optional<UserRole> findByName(String name);

}
