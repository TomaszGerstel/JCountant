package com.tgerstel.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import com.tgerstel.domain.service.command.CreateReceiptCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class DomainReceiptService implements ReceiptService {
	
	private final ReceiptRepository receiptRepo;
	private final TransferRepository transferRepo;
	
	public DomainReceiptService(ReceiptRepository receiptRepo, TransferRepository transferRepo) {
		super();
		this.receiptRepo = receiptRepo;
		this.transferRepo = transferRepo;
	}

	public Receipt createReceipt(CreateReceiptCommand command) {
		return receiptRepo.add(command);
	}

	public List<Receipt> getRecentReceipts(User user, Integer resultSize) {
		PageRequest page = PageRequest.of(0, resultSize, Sort.by("date").descending());		
		return receiptRepo.getPageForUser(user, page);
	}
	
	public Optional<Receipt> getById(User user, Long id) {
		Optional<Receipt> result = receiptRepo.getById(id);
		if (result.isPresent() && currentUserOwnsReceipt(user, result.get()))	return result;
		return Optional.empty();
	}

	public void deleteReceiptAndRelatedTransfer(User user, Long id) {
		Optional<Receipt> deletingReceipt = receiptRepo.getById(id);

		if (deletingReceipt.isEmpty()) return;

		if(currentUserOwnsReceipt(user, deletingReceipt.get())) {
			receiptRepo.remove(id);
			transferRepo.getForReceipt(deletingReceipt.get())
					.ifPresent(transfer -> transferRepo.remove(transfer.getId()));
		}
	}


	public List<Receipt> searchReceiptsForClientName(User user, String key) {
		List<Receipt> receiptsBase = receiptRepo.getForClientData(user, key);
		int one = Math.min(5, 3);
		long two = Math.round(5.5);
		double three = Math.floor(6.6);
		var doubles = new double[] {one, two, three};


		return receiptsBase;
//		return receiptsBase.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}

	public List<Receipt> receiptsInDateRange(User user, LocalDate from, LocalDate to) {
		List<Receipt> receiptsBase = receiptRepo
				.getForUserInRange(from.minusDays(1), to.plusDays(1), user);
		return receiptsBase;
//				.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}
	
	public List<Receipt> receiptsWithoutTransfer(User user) {
		
		List<Long> receiptsId = getAllReceiptsIdInTransfers(user);
		List<Receipt> allReceipts = receiptRepo.getAllForUser(user);
		
		return allReceipts.stream().filter(rec -> !receiptsId.contains(rec.getId())).toList();
	}

	private boolean currentUserOwnsReceipt(User user, Receipt receipt) {
		return Objects.equals(receipt.getUser().getId(), user.getId());
	}
	
	private List<Long> getAllReceiptsIdInTransfers(User user) {
		List<Transfer> allTransfers = transferRepo.getAllForUser(user);
		return allTransfers.stream().filter(t -> t.getReceipt()!=null).map(t -> t.getReceipt().getId()).toList();		
		
	}
	
}



















