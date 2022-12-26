package com.tgerstel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;
import com.tgerstel.repository.TransferRepository;

@Service
public class TransferService {

	private final TransferRepository transferRepo;
	private final ReceiptRepository receiptRepo;

	public TransferService(TransferRepository transferRepo, ReceiptRepository receiptRepo) {
		super();
		this.transferRepo = transferRepo;
		this.receiptRepo = receiptRepo;
	}

	public Optional<Transfer> createTransfer(Transfer transfer, Long receiptId, User user) {
		transfer.setUser(user);
		if (!transferTypeWithoutReceipt(transfer)) {
			Optional<Receipt> result = receiptRepo.findById(receiptId);
			if (result.isEmpty() || !currentUserOwnsReceipt(user, result))
				return Optional.empty();
			transfer.setReceipt(result.get());
		}
		return Optional.of(transferRepo.save(transfer));
	}

	public List<Transfer> getRecentTransfers(User user, Integer resultSize) {
		PageRequest page = PageRequest.of(0, resultSize, Sort.by("baseDate").descending());
		List<Transfer> transfers = transferRepo.findAllByUser(user, page);
		eraseUsers(transfers);
		return completeListTransfersDataFromReceipts(transfers);
	}

	public Optional<Transfer> getById(User user, Long id) {
		Optional<Transfer> result = transferRepo.findById(id);
		if (result.isPresent() && currentUserOwnsTransfer(user, result)) {
			completeTransferDataFromReceipt(result.get());
			return result;
		}
		return Optional.empty();
	}

	public void deleteTransfer(User user, Long id) {
		Optional<Transfer> deletingTransfer = transferRepo.findById(id);
		if (deletingTransfer.isPresent() && currentUserOwnsTransfer(user, deletingTransfer)) {
			transferRepo.deleteById(id);
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
	public List<Transfer> searchTransfersByFromName(User user, String key) {
		List<Transfer> transferBase = transferRepo.findAllByFromContainingIgnoreCase(key);
		// method
		return transferBase.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}

	private boolean transferTypeWithoutReceipt(Transfer transfer) {
		TransferType t = transfer.getTransferType();
		return (t == TransferType.SALARY || t == TransferType.TAX_OUT_TRANSFER || t == TransferType.VAT_OUT_TRANSFER);
	}
	
	protected void eraseUsers(List<Transfer> transfers) {
		for(Transfer t : transfers) eraseUserData(t);
	}	
	
	protected void eraseUserData(Transfer transfer) {
		transfer.setUser(null);
		transfer.getReceipt().setUser(null);
		
	}

}
