package com.tgerstel.domain.service;

import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.User;
import com.tgerstel.domain.service.command.CreateTransferCommand;

import java.util.List;
import java.util.Optional;

public interface TransferService {

    Optional<Transfer> createTransfer(CreateTransferCommand transfer);

    List<Transfer> getRecentTransfers(User user, Integer resultSize);

    List<Transfer> searchTransfersForUserWithSenderData(User user, String key);

    Optional<Transfer> getById(User user, Long id);

    void deleteTransfer(User user, Long id);
}
