package com.tgerstel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.User;


public interface ReceiptRepository extends CrudRepository<Receipt, Long>{


	List<Receipt> findAllByUser(User user, PageRequest page);	

	void deleteById(Long id);

	Optional<Receipt> findById(Long id);
	
	List<Receipt> findAllByClientContainingIgnoreCase(String key);


}