package com.tgerstel.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptSpringRepository extends CrudRepository<ReceiptEntity, Long>{

	List<ReceiptEntity> findAllByUserId(Long user_id, PageRequest page);

	List<ReceiptEntity> findAllByUserId(Long user_id);

	List<ReceiptEntity> findAllByDateAfterAndDateBeforeAndUserId(LocalDate minusDays, LocalDate plusDays, Long user_id);

	List<ReceiptEntity> findAllByUserIdAndClientContainingIgnoreCase(Long user_id, String key);


}