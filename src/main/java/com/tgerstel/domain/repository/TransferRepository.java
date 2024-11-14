package com.tgerstel.domain.repository;

import com.tgerstel.infrastructure.repository.Receipt;
import com.tgerstel.infrastructure.repository.Transfer;
import com.tgerstel.infrastructure.repository.User;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransferRepository {

    Optional<Transfer> getById(Long id);

    Transfer add(Transfer transfer);

    List<Transfer> getPageForUser(User user, PageRequest page);

    List<Transfer> getAllForUser(User user);

    Optional<Transfer> getForReceipt(Receipt receipt);;

    List<Transfer> getForSenderData(String key);

    List<Transfer> getForUserInRange(LocalDate from, LocalDate to, User user);

    void remove(Long id);

}
