package com.tgerstel.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptSpringRepository extends CrudRepository<ReceiptEntity, Long>{

	List<ReceiptEntity> findAllByUser(User user, PageRequest page);

	List<ReceiptEntity> findAllByUser(User user);

	List<ReceiptEntity> findAllByDateAfterAndDateBeforeAndUser(LocalDate minusDays, LocalDate plusDays, User user);

	List<ReceiptEntity> findAllByUserAndClientContainingIgnoreCase(User user, String key);


}