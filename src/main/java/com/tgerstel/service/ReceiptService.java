package com.tgerstel.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;

@Service
public class ReceiptService {
	
	private final ReceiptRepository receiptRepo;
	private final TransferService transferService;
	
	public ReceiptService(ReceiptRepository receiptRepo, TransferService transferService) {
		super();
		this.receiptRepo = receiptRepo;
		this.transferService = transferService;
	}


	public Receipt createReceipt(Receipt receipt, User currentUser) {
		
		receipt.setUser(currentUser);		 	
		return receiptRepo.save(receipt);	
	}

	public List<Receipt> getRecentReceipts(User user, Integer resultSize) {
		
		PageRequest page = PageRequest.of(0, resultSize, Sort.by("date").descending());		
		return receiptRepo.findAllByUser(user, page);
	}
	
	public Optional<Receipt> getById(User user, Long id) {
		
		Optional<Receipt> result = receiptRepo.findById(id);		
		if (result.isPresent() && currentUserOwnsReceipt(user, result))	return result;
		return Optional.empty();
	}

	public void deleteReceipt(User user, Long id) {			
		
		Optional<Receipt> deletingReceipt = receiptRepo.findById(id);
		Optional<Transfer> deletingTransfer = transferService.getByReceipt(deletingReceipt);
		if(deletingReceipt.isPresent() && currentUserOwnsReceipt(user, deletingReceipt)) {
			receiptRepo.deleteById(id);
		}
		transferService.deleteTransfer(user, deletingTransfer.get().getId());
	}
	
	boolean currentUserOwnsReceipt(User user, Optional<Receipt> receipt) {
		if(receipt.get().getUser().getId() == user.getId()) return true;
		return false;
	}
	
	public List<Receipt> searchReceiptsByClientName(User user, String key) {
		
		List<Receipt> receiptsBase = receiptRepo.findAllByClientContainingIgnoreCase(key);	
		return receiptsBase.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}
	
	public List<Receipt> receiptsInDateRange(User user, LocalDate from, LocalDate to) {
		List<Receipt> receiptsBase = receiptRepo.findAllByDateAfterAndDateBefore(from, to);
		return receiptsBase.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}
	
	public List<Receipt> receiptsNotUsedInTransfer(User user) {
		
		List<Long> receiptsId = transferService.getAllReceiptsIdInTransfers();
		List<Receipt> allReceipts = receiptRepo.findAllByUser();
		
		return allReceipts.stream().filter(rec -> !receiptsId.contains(rec.getId())).toList();
	}
	
	
	
}



















