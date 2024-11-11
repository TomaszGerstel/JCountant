package com.tgerstel.domain.service;

import com.tgerstel.infrastructure.repository.Transfer;
import com.tgerstel.infrastructure.repository.User;

import java.util.List;
import java.util.Optional;

public interface TransferService {

    Optional<Transfer> createTransfer(Transfer transfer, Long receiptId, User user);

    List<Transfer> getRecentTransfers(User user, Integer resultSize);

    List<Transfer> searchTransfersForUserWithSenderData(User user, String key);

    Optional<Transfer> getById(User user, Long id);

    void deleteTransfer(User user, Long id);
}
