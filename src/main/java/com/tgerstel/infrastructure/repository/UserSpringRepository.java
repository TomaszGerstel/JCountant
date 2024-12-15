package com.tgerstel.infrastructure.repository;

import org.springframework.data.repository.CrudRepository;

public interface UserSpringRepository extends CrudRepository<UserEntity, Long>{

	UserEntity findByUsername(String username);

}
