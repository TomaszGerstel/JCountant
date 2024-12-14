package com.tgerstel.domain.service;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.service.command.CreateReceiptCommand;
import com.tgerstel.infrastructure.repository.User;

import java.util.List;
import java.util.Optional;

public interface ReceiptService {

    Receipt createReceipt(CreateReceiptCommand receipt);

    List<Receipt> getRecentReceipts(User user, Integer resultSize);

    Optional<Receipt> getById(User user, Long id);

    void deleteReceiptAndRelatedTransfer(User user, Long id);

    List<Receipt> searchReceiptsForClientName(User user, String key);

    List<Receipt> receiptsWithoutTransfer(User user);

}
