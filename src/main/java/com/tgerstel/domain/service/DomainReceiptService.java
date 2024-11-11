package com.tgerstel.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tgerstel.infrastructure.repository.Receipt;
import com.tgerstel.infrastructure.repository.Transfer;
import com.tgerstel.infrastructure.repository.User;

@Service
public class DomainReceiptService implements ReceiptService {
	
	private final ReceiptRepository receiptRepo;
	private final TransferRepository transferRepo;
	
	public DomainReceiptService(ReceiptRepository receiptRepo, TransferRepository transferRepo) {
		super();
		this.receiptRepo = receiptRepo;
		this.transferRepo = transferRepo;
	}

	public Receipt createReceipt(Receipt receipt, User currentUser) {		
		receipt.setUser(currentUser);		 	
		return receiptRepo.add(receipt);
	}

	public List<Receipt> getRecentReceipts(User user, Integer resultSize) {		
		PageRequest page = PageRequest.of(0, resultSize, Sort.by("date").descending());		
		return receiptRepo.getPageForUser(user, page);
	}
	
	public Optional<Receipt> getById(User user, Long id) {		
		Optional<Receipt> result = receiptRepo.getById(id);
		if (result.isPresent() && currentUserOwnsReceipt(user, result))	return result;
		return Optional.empty();
	}

	public void deleteReceiptAndRelatedTransfer(User user, Long id) {		
		Optional<Receipt> deletingReceipt = receiptRepo.getById(id);

		if (deletingReceipt.isEmpty()) return;

		Optional<Transfer> deletingTransfer = transferRepo.getForReceipt(deletingReceipt.get());
        deletingTransfer.ifPresent(transfer -> transferRepo.remove(transfer.getId()));

		if(currentUserOwnsReceipt(user, deletingReceipt)) receiptRepo.remove(id);
		
	}
	
	boolean currentUserOwnsReceipt(User user, Optional<Receipt> receipt) {
		if(receipt.get().getUser().getId() == user.getId()) return true;
		return false;
	}
	//test
	public List<Receipt> searchReceiptsForClientName(User user, String key) {
		List<Receipt> receiptsBase = receiptRepo.getForClientData(user, key);
		return receiptsBase;
//		return receiptsBase.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}
	//test, not used?
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
	
	private List<Long> getAllReceiptsIdInTransfers(User user) {
		List<Transfer> allTransfers = transferRepo.getAllForUser(user);
		return allTransfers.stream().filter(t -> t.getReceipt()!=null).map(t -> t.getReceipt().getId()).toList();		
		
	}
	
}



















