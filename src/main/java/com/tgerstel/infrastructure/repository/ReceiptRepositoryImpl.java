package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.service.command.CreateReceiptCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReceiptRepositoryImpl implements ReceiptRepository {

    private final ReceiptSpringRepository receiptSpringRepository;

    public ReceiptRepositoryImpl(final ReceiptSpringRepository receiptSpringRepository) {
        this.receiptSpringRepository = receiptSpringRepository;
    }

    @Override
    public Optional<Receipt> getById(final Long id) {
        Optional<ReceiptEntity> entity = receiptSpringRepository.findById(id);
        return entity.map(ReceiptEntity::toReceipt);
    }

    @Override
    public Receipt add(CreateReceiptCommand command) {
        ReceiptEntity receiptToSave = new ReceiptEntity(command.date(), command.amount(), command.netAmount(),
                command.vatValue(), command.vatPercentage(), command.client(), command.worker(), command.description(),
                command.user());
        return receiptSpringRepository.save(receiptToSave).toReceipt();
    }


    @Override
    public List<Receipt> getPageForUser(final User user, final PageRequest page) {
        return receiptSpringRepository.findAllByUser(user, page)
                .stream().map(ReceiptEntity::toReceipt).collect(Collectors.toList());
    }

    @Override
    public List<Receipt> getAllForUser(final User user) {
        return receiptSpringRepository.findAllByUser(user).stream().map(ReceiptEntity::toReceipt)
                .collect(Collectors.toList());
    }

    @Override
    public List<Receipt> getForUserInRange(final LocalDate from, final LocalDate to, final User user) {
        return receiptSpringRepository.findAllByDateAfterAndDateBeforeAndUser(from, to, user)
                .stream().map(ReceiptEntity::toReceipt).collect(Collectors.toList());
    }

    @Override
    public List<Receipt> getForClientData(final User user, final String key) {
        return receiptSpringRepository.findAllByUserAndClientContainingIgnoreCase(user, key)
                .stream().map(ReceiptEntity::toReceipt).collect(Collectors.toList());
    }

    @Override
    public void remove(Long id) {
        receiptSpringRepository.deleteById(id);
    }
}
