package com.tgerstel.infrastructure.repository;


import com.tgerstel.domain.repository.TransferRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TransferRepositoryImpl implements TransferRepository {

    private final TransferSpringRepository transferSpringRepository;

    public TransferRepositoryImpl(final TransferSpringRepository transferSpringRepository) {
        this.transferSpringRepository = transferSpringRepository;
    }

    @Override
    public Optional<Transfer> getById(Long id) {
        return transferSpringRepository.findById(id);
    }

    @Override
    public Transfer add(Transfer transfer) {
        return transferSpringRepository.save(transfer);
    }

    @Override
    public List<Transfer> getPageForUser(final User user, final PageRequest page) {
        return transferSpringRepository.findAllByUser(user, page);
    }

    @Override
    public List<Transfer> getAllForUser(final User user) {
        return transferSpringRepository.findAllByUser(user);
    }

    @Override
    public Optional<Transfer> getForReceipt(final Receipt receipt) {
        return transferSpringRepository.findByReceipt(receipt);
    }

    @Override
    public List<Transfer> getForSenderData(final String key) {
        return transferSpringRepository.findAllByFromContainingIgnoreCase(key);
    }

    @Override
    public List<Transfer> getForUserInRange(final LocalDate from, final LocalDate to, final User user) {
        return transferSpringRepository.findAllByDateAfterAndDateBeforeAndUser(from, to, user);
    }

    @Override
    public void remove(Long id) {
        transferSpringRepository.deleteById(id);
    }
}
