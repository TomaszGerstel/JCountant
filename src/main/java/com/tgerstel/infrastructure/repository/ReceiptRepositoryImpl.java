package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.repository.ReceiptRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReceiptRepositoryImpl implements ReceiptRepository {

    private final ReceiptSpringRepository receiptSpringRepository;

    public ReceiptRepositoryImpl(final ReceiptSpringRepository receiptSpringRepository) {
        this.receiptSpringRepository = receiptSpringRepository;
    }

    @Override
    public Optional<Receipt> getById(final Long id) {
        return receiptSpringRepository.findById(id);
    }

    @Override
    public Receipt add(Receipt receipt) {
        return receiptSpringRepository.save(receipt);
    }


    @Override
    public List<Receipt> getPageForUser(final User user, final PageRequest page) {
        return receiptSpringRepository.findAllByUser(user, page);
    }

    @Override
    public List<Receipt> getAllForUser(final User user) {
        return receiptSpringRepository.findAllByUser(user);
    }

    @Override
    public List<Receipt> getForUserInRange(final LocalDate from, final LocalDate to, final User user) {
        return receiptSpringRepository.findAllByDateAfterAndDateBeforeAndUser(from, to, user);
    }

    @Override
    public List<Receipt> getForClientData(final User user, final String key) {
        return receiptSpringRepository.findAllByUserAndClientContainingIgnoreCase(user, key);
    }

    @Override
    public void remove(Long id) {
        receiptSpringRepository.deleteById(id);
    }
}
