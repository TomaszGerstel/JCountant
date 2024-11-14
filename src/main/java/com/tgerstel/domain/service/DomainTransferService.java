package com.tgerstel.domain.service;

import java.util.List;
import java.util.Optional;

import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.tgerstel.infrastructure.repository.Receipt;
import com.tgerstel.infrastructure.repository.Transfer;
import com.tgerstel.domain.TransferType;
import com.tgerstel.infrastructure.repository.User;

public class DomainTransferService implements TransferService {

	private final TransferRepository transferRepo;
	private final ReceiptRepository receiptRepo;

	public DomainTransferService(TransferRepository transferRepo, ReceiptRepository receiptRepo) {
		super();
		this.transferRepo = transferRepo;
		this.receiptRepo = receiptRepo;
	}

	public Optional<Transfer> createTransfer(Transfer transfer, Long receiptId, User user) {
		transfer.setUser(user);
		if (!transferTypeWithoutReceipt(transfer)) {
			Optional<Receipt> result = receiptRepo.getById(receiptId);
			if (result.isEmpty() || !currentUserOwnsReceipt(user, result) || receiptUsedInTransfer(result.get()))
				return Optional.empty();
			transfer.setReceipt(result.get());
		}
		return Optional.of(transferRepo.add(transfer));
	}
	
	protected boolean receiptUsedInTransfer(Receipt receipt) {
		Optional<Transfer> result = transferRepo.getForReceipt(receipt);
		return !result.isEmpty();
	}

	public List<Transfer> getRecentTransfers(User user, Integer resultSize) {
		PageRequest page = PageRequest.of(0, resultSize, Sort.by("baseDate").descending());
		List<Transfer> transfers = transferRepo.getPageForUser(user, page);
		return completeListTransfersDataFromReceipts(transfers);
	}

	public Optional<Transfer> getById(User user, Long id) {
		Optional<Transfer> result = transferRepo.getById(id);
		if (result.isPresent() && currentUserOwnsTransfer(user, result)) {
			completeTransferDataFromReceipt(result.get());
			return result;
		}
		return Optional.empty();
	}

	public void deleteTransfer(User user, Long id) {
		Optional<Transfer> deletingTransfer = transferRepo.getById(id);
		if (deletingTransfer.isPresent() && currentUserOwnsTransfer(user, deletingTransfer)) {
			transferRepo.remove(id);
		}
	}

	// test
	boolean currentUserOwnsTransfer(User user, Optional<Transfer> transfer) {
		if (transfer.get().getUser().getId() == user.getId())
			return true;
		return false;
	}

	boolean currentUserOwnsReceipt(User user, Optional<Receipt> receipt) {
		if (receipt.get().getUser().getId() == user.getId())
			return true;
		return false;
	}

	// test
	private List<Transfer> completeListTransfersDataFromReceipts(List<Transfer> transfers) {
		for (Transfer t : transfers)
			completeTransferDataFromReceipt(t);
		return transfers;
	}

	// test
	private Optional<Transfer> completeTransferDataFromReceipt(Transfer transfer) {
		if (transfer.getReceipt() == null)
			return Optional.of(transfer);
		if (transfer.getDate() == null)
			transfer.setDate(transfer.getReceipt().getDate());
		if (transfer.getDescription() == null)
			transfer.setDescription(transfer.getReceipt().getDescription());
		if (transfer.getFrom() == null)
			transfer.setFrom(transfer.getReceipt().getClient());
		if (transfer.getTo() == null)
			transfer.setTo(transfer.getReceipt().getWorker());
		return Optional.of(transfer);
	}

	// test
	public List<Transfer> searchTransfersForUserWithSenderData(User user, String key) {
		List<Transfer> transferBase = transferRepo.getForSenderData(key);
		// method
		System.out.println("service trans "+transferBase);
		return transferBase.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}

	private boolean transferTypeWithoutReceipt(Transfer transfer) {
		TransferType t = transfer.getTransferType();
		return (t == TransferType.SALARY || t == TransferType.TAX_OUT_TRANSFER || t == TransferType.VAT_OUT_TRANSFER);
	}

}
