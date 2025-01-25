package com.tgerstel.domain.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import com.tgerstel.domain.service.command.CreateTransferCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.tgerstel.domain.TransferType;

public class DomainTransferService implements TransferService {

    private final TransferRepository transferRepository;
    private final ReceiptRepository receiptRepository;

    private static final List<TransferType> TRANSFERS_WITHOUT_RECEIPT =
            List.of(TransferType.SALARY, TransferType.TAX_OUT_TRANSFER, TransferType.VAT_OUT_TRANSFER);

    public DomainTransferService(TransferRepository transferRepository, ReceiptRepository receiptRepository) {
        super();
        this.transferRepository = transferRepository;
        this.receiptRepository = receiptRepository;
    }

    public Optional<Transfer> createTransfer(final CreateTransferCommand command) {
        if (!isReceiptNotRequiredForTransfer(command.transferType())) {
            final Optional<Receipt> receipt = receiptRepository.getById(command.receiptId());
            if (receipt.isEmpty() || !isOwnedByCurrentUser(command.user(), receipt.get().getUser())
                    || isReceiptUsedInTransfer(receipt.get()))
                return Optional.empty();
        }
        return Optional.of(transferRepository.add(command));
    }

    public List<Transfer> getRecentTransfers(final User user, final Integer resultSize) {
        final PageRequest pageRequest = PageRequest.of(0, resultSize, Sort.by("baseDate").descending());
        return completeTransfersDataFromReceipts(transferRepository.getPageForUser(user, pageRequest));
    }

    public Optional<Transfer> getById(final User user, final Long id) {
        final Optional<Transfer> transfer =
                transferRepository.getById(id).filter(t -> isOwnedByCurrentUser(user, t.getUser()));
        transfer.ifPresent(this::completeTransferDataFromReceipt);
        return transfer;
    }

    public void deleteTransfer(final User user, final Long id) {
        transferRepository.getById(id)
                .filter(transfer -> isOwnedByCurrentUser(user, transfer.getUser()))
                .ifPresent(transfer -> transferRepository.remove(id));
    }

    public List<Transfer> searchTransfersForUserWithSenderData(final User user, final String key) {
        return transferRepository.getForSenderData(key).stream()
                .filter(transfer -> isOwnedByCurrentUser(user, transfer.getUser()))
                .toList();
    }

    private boolean isReceiptUsedInTransfer(final Receipt receipt) {
        return transferRepository.getForReceipt(receipt).isPresent();
    }

    private boolean isOwnedByCurrentUser(final User currentUser, final User owner) {
        return Objects.equals(currentUser.getId(), owner.getId());
    }

    private List<Transfer> completeTransfersDataFromReceipts(final List<Transfer> transfers) {
        transfers.forEach(this::completeTransferDataFromReceipt);
        return transfers;
    }

    private void completeTransferDataFromReceipt(final Transfer transfer) {
        if (transfer.getReceipt() != null) {
            setDefaultIfNull(transfer::getDate, transfer.getReceipt()::getDate, transfer::setDate);
            setDefaultIfNull(transfer::getDescription, transfer.getReceipt()::getDescription, transfer::setDescription);
            setDefaultIfNull(transfer::getFrom, transfer.getReceipt()::getClient, transfer::setFrom);
            setDefaultIfNull(transfer::getTo, transfer.getReceipt()::getWorker, transfer::setTo);
        }
    }

    private <T> void setDefaultIfNull(final Supplier<T> getter, final Supplier<T> defaultSupplier,
                                      final  Consumer<T> setter) {
        if (getter.get() == null) {
            setter.accept(defaultSupplier.get());
        }
    }

    private boolean isReceiptNotRequiredForTransfer(final TransferType t) {
        return TRANSFERS_WITHOUT_RECEIPT.contains(t);
    }

}
