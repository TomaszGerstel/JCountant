package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.User;
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
    public Receipt add(final CreateReceiptCommand command) {
        ReceiptEntity receiptToSave = new ReceiptEntity(command.date(), command.amount(), command.netAmount(),
                command.vatValue(), command.vatPercentage(), command.client(), command.worker(), command.description(),
                new UserEntity(command.user()));
        return receiptSpringRepository.save(receiptToSave).toReceipt();
    }


    @Override
    public List<Receipt> getPageForUser(final User user, final PageRequest page) {
        UserEntity userEntity = new UserEntity(user.getUsername(), user.getEmail(), user.getPassword(), user.getLumpSumTaxRate());
        return receiptSpringRepository.findAllByUserId(user.getId(), page)
                .stream().map(ReceiptEntity::toReceipt).collect(Collectors.toList());
    }

    @Override
    public List<Receipt> getAllForUser(final User user) {
        UserEntity userEntity = new UserEntity(user.getUsername(), user.getEmail(), user.getPassword(), user.getLumpSumTaxRate());
        return receiptSpringRepository.findAllByUserId(user.getId()).stream().map(ReceiptEntity::toReceipt)
                .collect(Collectors.toList());
    }

    @Override
    public List<Receipt> getForUserInRange(final LocalDate from, final LocalDate to, final User user) {
        return receiptSpringRepository.findAllByDateAfterAndDateBeforeAndUserId(from, to, user.getId())
                .stream().map(ReceiptEntity::toReceipt).collect(Collectors.toList());
    }

    @Override
    public List<Receipt> getForClientData(final User user, final String key) {
        return receiptSpringRepository.findAllByUserIdAndClientContainingIgnoreCase(user.getId(), key)
                .stream().map(ReceiptEntity::toReceipt).collect(Collectors.toList());
    }

    @Override
    public void remove(Long id) {
        receiptSpringRepository.deleteById(id);
    }
}
