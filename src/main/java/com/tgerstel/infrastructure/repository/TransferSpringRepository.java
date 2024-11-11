package com.tgerstel.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

public interface TransferSpringRepository extends CrudRepository<Transfer, Long>{

	List<Transfer> findAllByUser(User user, PageRequest page);

	Optional<Transfer> findByReceipt(Receipt receipt);

	List<Transfer> findAllByFromContainingIgnoreCase(String key);

	List<Transfer> findAllByUser(User user);

	List<Transfer> findAllByDateAfterAndDateBeforeAndUser(LocalDate minusDays, LocalDate plusDays, User user);	

}
