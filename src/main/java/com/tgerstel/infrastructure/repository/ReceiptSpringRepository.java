package com.tgerstel.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptSpringRepository extends CrudRepository<Receipt, Long>{

	List<Receipt> findAllByUser(User user, PageRequest page);

	List<Receipt> findAllByUser(User user);

	List<Receipt> findAllByDateAfterAndDateBeforeAndUser(LocalDate minusDays, LocalDate plusDays, User user);

	List<Receipt> findAllByUserAndClientContainingIgnoreCase(User user,String key);


}