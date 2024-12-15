package com.tgerstel.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

public interface TransferSpringRepository extends CrudRepository<TransferEntity, Long>{

	List<TransferEntity> findAllByUserId(Long userId, PageRequest page);

	Optional<TransferEntity> findByReceiptEntityId(Long receiptId);

	List<TransferEntity> findAllByFromContainingIgnoreCase(String key);

	List<TransferEntity> findAllByUserId(Long userId);

	List<TransferEntity> findAllByDateAfterAndDateBeforeAndUserId(LocalDate minusDays, LocalDate plusDays, Long userId);

}
