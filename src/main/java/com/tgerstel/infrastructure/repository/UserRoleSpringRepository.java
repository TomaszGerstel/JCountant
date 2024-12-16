package com.tgerstel.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRoleSpringRepository extends CrudRepository<UserRoleEntity, Long>{

	Optional<UserRoleEntity> findByName(String name);

}
