package com.tgerstel.repository;

import org.springframework.data.repository.CrudRepository;

import com.tgerstel.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	User findByUsername(String username);

}
