package com.tgerstel.infrastructure.repository;


import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.repository.TransferRepository;
import com.tgerstel.domain.service.command.CreateTransferCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TransferRepositoryImpl implements TransferRepository {

    private final TransferSpringRepository transferSpringRepository;
    private final ReceiptSpringRepository receiptSpringRepository;

    public TransferRepositoryImpl(final TransferSpringRepository transferSpringRepository, ReceiptSpringRepository receiptSpringRepository) {
        this.transferSpringRepository = transferSpringRepository;
        this.receiptSpringRepository = receiptSpringRepository;
    }

    @Override
    public Optional<Transfer> getById(Long id) {
        Optional<TransferEntity> entity = transferSpringRepository.findById(id);
        return entity.map(TransferEntity::toTransfer);
    }

    @Override
    public Transfer add(CreateTransferCommand command) {
        Long receiptId = command.receiptId();
        ReceiptEntity receipt =
                receiptId == null ? null : receiptSpringRepository.findById(receiptId).orElse(null);
        TransferEntity transferEntity = new TransferEntity(command.transferType(), command.amount(), command.from(),
                command.to(), command.date(), command.description(), receipt, command.user());
        return transferSpringRepository.save(transferEntity).toTransfer();
    }

    @Override
    public List<Transfer> getPageForUser(final User user, final PageRequest page) {
        return transferSpringRepository.findAllByUser(user, page)
                .stream().map(TransferEntity::toTransfer).toList();
    }

    @Override
    public List<Transfer> getAllForUser(final User user) {
        return transferSpringRepository.findAllByUser(user)
                .stream().map(TransferEntity::toTransfer).toList();
    }

    @Override
    public Optional<Transfer> getForReceipt(final Receipt receipt) {
        return transferSpringRepository.findByReceiptEntityId(receipt.getId()).map(TransferEntity::toTransfer);
    }

    @Override
    public List<Transfer> getForSenderData(final String key) {
        return transferSpringRepository.findAllByFromContainingIgnoreCase(key)
                .stream().map(TransferEntity::toTransfer).toList();
    }

    @Override
    public List<Transfer> getForUserInRange(final LocalDate from, final LocalDate to, final User user) {
        return transferSpringRepository.findAllByDateAfterAndDateBeforeAndUser(from, to, user)
                .stream().map(TransferEntity::toTransfer).toList();
    }

    @Override
    public void remove(Long id) {
        transferSpringRepository.deleteById(id);
    }
}
