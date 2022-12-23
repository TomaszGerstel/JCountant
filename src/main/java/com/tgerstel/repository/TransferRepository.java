package com.tgerstel.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.User;

public interface TransferRepository extends CrudRepository<Transfer, Long>{

	List<Transfer> findAllByUser(User user, PageRequest page);

	Optional<Transfer> findByReceipt(Optional<Receipt> receipt);
	
	List<Transfer> findAll();

	List<Transfer> findAllByFromContainingIgnoreCase(String key);

	List<Transfer> findAllByUser(User user);

	List<Transfer> findAllByDateAfterAndDateBefore(LocalDate minusDays, LocalDate plusDays);

	List<Transfer> findAllByDateAfterAndDateBeforeAndUser(LocalDate minusDays, LocalDate plusDays, User user);	

}
