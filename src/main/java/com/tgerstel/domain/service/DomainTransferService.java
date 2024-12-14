package com.tgerstel.domain.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import com.tgerstel.domain.service.command.CreateTransferCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.tgerstel.domain.TransferType;
import com.tgerstel.infrastructure.repository.User;

public class DomainTransferService implements TransferService {

	private final TransferRepository transferRepository;
	private final ReceiptRepository receiptRepository;

	public DomainTransferService(TransferRepository transferRepository, ReceiptRepository receiptRepository) {
		super();
		this.transferRepository = transferRepository;
		this.receiptRepository = receiptRepository;
	}

	public Optional<Transfer> createTransfer(CreateTransferCommand command) {
		if (!transferTypeWithoutReceipt(command.transferType())) {
			Optional<Receipt> receipt = receiptRepository.getById(command.receiptId());
			if (receipt.isEmpty() || !currentUserOwnsReceipt(command.user(), receipt.get())
					|| receiptUsedInTransfer(receipt.get()))
				return Optional.empty();
		}
		return Optional.of(transferRepository.add(command));
	}

	public List<Transfer> getRecentTransfers(User user, Integer resultSize) {
		PageRequest page = PageRequest.of(0, resultSize, Sort.by("baseDate").descending());
		List<Transfer> transfers = transferRepository.getPageForUser(user, page);
		return completeListTransfersDataFromReceipts(transfers);
	}

	public Optional<Transfer> getById(User user, Long id) {
		Optional<Transfer> result = transferRepository.getById(id);
		if (result.isPresent() && currentUserOwnsTransfer(user, result)) {
			completeTransferDataFromReceipt(result.get());
			return result;
		}
		return Optional.empty();
	}

	public void deleteTransfer(User user, Long id) {
		Optional<Transfer> deletingTransfer = transferRepository.getById(id);
		if (deletingTransfer.isPresent() && currentUserOwnsTransfer(user, deletingTransfer)) {
			transferRepository.remove(id);
		}
	}

	public List<Transfer> searchTransfersForUserWithSenderData(User user, String key) {
		List<Transfer> transferBase = transferRepository.getForSenderData(key);
		// method
		System.out.println("service trans "+transferBase);
		return transferBase.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}

	private boolean receiptUsedInTransfer(Receipt receipt) {
		Optional<Transfer> result = transferRepository.getForReceipt(receipt);
		return result.isPresent();
	}

	private boolean currentUserOwnsTransfer(User user, Optional<Transfer> transfer) {
		if (transfer.get().getUser().getId() == user.getId())
			return true;
		return false;
	}

	private boolean currentUserOwnsReceipt(User user, Receipt receipt) {
        return Objects.equals(receipt.getUser().getId(), user.getId());
    }

	private List<Transfer> completeListTransfersDataFromReceipts(List<Transfer> transfers) {
		for (Transfer t : transfers)
			completeTransferDataFromReceipt(t);
		return transfers;
	}

	private void completeTransferDataFromReceipt(Transfer transfer) {
		if (transfer.getReceipt() == null)
			return;
		if (transfer.getDate() == null)
			transfer.setDate(transfer.getReceipt().getDate());
		if (transfer.getDescription() == null)
			transfer.setDescription(transfer.getReceipt().getDescription());
		if (transfer.getFrom() == null)
			transfer.setFrom(transfer.getReceipt().getClient());
		if (transfer.getTo() == null)
			transfer.setTo(transfer.getReceipt().getWorker());
	}

	private boolean transferTypeWithoutReceipt(TransferType t) {
		return (t == TransferType.SALARY || t == TransferType.TAX_OUT_TRANSFER || t == TransferType.VAT_OUT_TRANSFER);
	}

}
