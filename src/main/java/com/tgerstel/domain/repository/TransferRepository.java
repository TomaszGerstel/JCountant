package com.tgerstel.domain.repository;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.User;
import com.tgerstel.domain.service.command.CreateTransferCommand;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransferRepository {

    Optional<Transfer> getById(Long id);

    Transfer add(CreateTransferCommand command);

    List<Transfer> getPageForUser(User user, PageRequest page);

    List<Transfer> getAllForUser(User user);

    Optional<Transfer> getForReceipt(Receipt receipt);

    List<Transfer> getForSenderData(String key);

    List<Transfer> getForUserInRange(LocalDate from, LocalDate to, User user);

    void remove(Long id);

}
