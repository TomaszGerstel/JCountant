package com.tgerstel.domain.repository;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.User;
import com.tgerstel.domain.service.command.CreateReceiptCommand;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReceiptRepository {

    Optional<Receipt> getById(Long id);

    Receipt add(CreateReceiptCommand receipt);

    List<Receipt> getPageForUser(User user, PageRequest page);

    List<Receipt> getAllForUser(User user);

    List<Receipt> getForUserInRange(LocalDate from, LocalDate to, User user);

    List<Receipt> getForClientData(User user, String key);

    void remove(Long id);


}
