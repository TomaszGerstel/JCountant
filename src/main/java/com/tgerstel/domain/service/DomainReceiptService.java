package com.tgerstel.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import com.tgerstel.domain.service.command.CreateReceiptCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class DomainReceiptService implements ReceiptService {
	
	private final ReceiptRepository receiptRepo;
	private final TransferRepository transferRepo;
	
	public DomainReceiptService(final ReceiptRepository receiptRepo, final TransferRepository transferRepo) {
		super();
		this.receiptRepo = receiptRepo;
		this.transferRepo = transferRepo;
	}

	public Receipt createReceipt(final CreateReceiptCommand command) {
		return receiptRepo.add(command);
	}

	public List<Receipt> getRecentReceipts(final User user, final Integer resultSize) {
		final PageRequest page = PageRequest.of(0, resultSize, Sort.by("date").descending());
		return receiptRepo.getPageForUser(user, page);
	}

	public Optional<Receipt> getById(final User user, final Long id) {
		return receiptRepo.getById(id).filter(receipt -> currentUserOwnsReceipt(user, receipt));
	}

	public void deleteReceiptAndRelatedTransfer(final User user, final Long id) {
		receiptRepo.getById(id).ifPresent(receipt -> {
			if (currentUserOwnsReceipt(user, receipt)) {
				receiptRepo.remove(id);
				transferRepo.getForReceipt(receipt)
						.ifPresent(transfer -> transferRepo.remove(transfer.getId()));
			}
		});
	}

	public List<Receipt> searchReceiptsForClientName(final User user, final String key) {
        return receiptRepo.getForClientData(user, key);
	}

	public List<Receipt> receiptsInDateRange(final User user, final LocalDate from, final LocalDate to) {
        return receiptRepo.getForUserInRange(from.minusDays(1), to.plusDays(1), user);
	}

	public List<Receipt> receiptsWithoutTransfer(final User user) {
		final List<Long> receiptIdsInTransfers = getAllReceiptIdsInTransfers(user);
		return receiptRepo.getAllForUser(user).stream()
				.filter(receipt -> !receiptIdsInTransfers.contains(receipt.getId()))
				.toList();
	}

	private boolean currentUserOwnsReceipt(final User user, final Receipt receipt) {
		return Objects.equals(receipt.getUser().getId(), user.getId());
	}

	private List<Long> getAllReceiptIdsInTransfers(final User user) {
		return transferRepo.getAllForUser(user).stream()
				.filter(transfer -> transfer.getReceipt() != null)
				.map(transfer -> transfer.getReceipt().getId())
				.toList();
	}
	
}



















