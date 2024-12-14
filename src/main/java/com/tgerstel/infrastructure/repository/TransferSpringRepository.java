package com.tgerstel.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

public interface TransferSpringRepository extends CrudRepository<TransferEntity, Long>{

	List<TransferEntity> findAllByUser(User user, PageRequest page);

	Optional<TransferEntity> findByReceiptEntityId(Long receiptId);

	List<TransferEntity> findAllByFromContainingIgnoreCase(String key);

	List<TransferEntity> findAllByUser(User user);

	List<TransferEntity> findAllByDateAfterAndDateBeforeAndUser(LocalDate minusDays, LocalDate plusDays, User user);

}
