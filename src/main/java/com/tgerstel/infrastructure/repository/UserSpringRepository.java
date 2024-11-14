package com.tgerstel.infrastructure.repository;

import org.springframework.data.repository.CrudRepository;

public interface UserSpringRepository extends CrudRepository<User, Long>{

	User findByUsername(String username);

}
